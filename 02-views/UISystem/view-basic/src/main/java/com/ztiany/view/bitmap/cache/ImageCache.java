package com.ztiany.view.bitmap.cache;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;

import com.ztiany.view.BuildConfig;
import com.ztiany.view.bitmap.cache.disk.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImageCache {

    private static ImageCache instance;

    /**
     * 单例类
     *
     * @return
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            synchronized (ImageCache.class) {
                if (instance == null) {
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    private LruCache<String, Bitmap> lruCache;
    private Set<WeakReference<Bitmap>> reusablePool;
    private DiskLruCache diskLruCache;
    private ReferenceQueue<Bitmap> referenceQueue;
    private volatile boolean shutDown;

    public void init(Context context, String dir) {
        // 复用池
        reusablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());

        // 内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int memoryClass = am.getMemoryClass();

        //memoryClass * 1024 * 1024 / 8
        lruCache = new LruCache<String, Bitmap>(memoryClass * 1024 * 1024 / 8) {
            // 返回的一张图片大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount();
                }
                return value.getByteCount();
            }

            /*
            1.  能够复用的时候（oldValue.isMutable() 就是判断能不能复用），我们就通过复用池来复用。
            2.  如果不能复用，我们就直接调用 recycler 回收
             */
            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                if (oldValue.isMutable()) {
                    // 3.0 bitmap 缓存 native
                    // <8.0  bitmap 缓存 java
                    // 8.0 native
                    reusablePool.add(new WeakReference<>(oldValue, getReferenceQueue()));
                }
                oldValue.recycle();
            }
        };
        try {
            diskLruCache = DiskLruCache.open(new File(dir), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 放入磁盘缓存
     */
    public void putBitmap2Disk(String key, Bitmap bitmap) {
        OutputStream os = null;

        try (DiskLruCache.Snapshot snapshot = diskLruCache.get(key)) {
            if (snapshot == null) {
                DiskLruCache.Editor edit = diskLruCache.edit(key);
                if (edit != null) {
                    os = edit.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                    edit.commit();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从磁盘缓存获取bitmap
     *
     * @param key
     * @param reusable
     * @return
     */
    public Bitmap getBitmapFromDisk(String key, Bitmap reusable) {
        Bitmap bitmap = null;
        try (DiskLruCache.Snapshot snapshot = diskLruCache.get(key)) {
            if (snapshot == null) {
                return null;
            }
            InputStream is = snapshot.getInputStream(0);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inMutable = true;
            options.inBitmap = reusable;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (bitmap != null) {
                lruCache.put(key, bitmap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private ReferenceQueue<Bitmap> getReferenceQueue() {
        if (referenceQueue == null) {
            referenceQueue = new ReferenceQueue<>();
            new Thread(() -> {
                while (!shutDown) {
                    try {
                        Reference<? extends Bitmap> remove = referenceQueue.remove();
                        Bitmap bitmap = remove.get();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            bitmap.recycle();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        return referenceQueue;
    }

    /**
     * 把bitmap 放入内存
     *
     * @param key
     * @param bitmap
     */
    public void putBitmap2Memory(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    /**
     * 从内存中取出bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromMemory(String key) {
        return lruCache.get(key);
    }

    public void clearMemory() {
        lruCache.evictAll();
    }

    /**
     * 3.0 之前不能复用
     * 3.0-4.4 宽高一样，inSampleSize = 1
     * 4.4 只要小于等于就行了
     *
     * @param w
     * @param h
     * @param inSampleSize
     * @return
     */
    public Bitmap getReusable(int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null;
        }
        Bitmap reusable = null;

        Iterator<WeakReference<Bitmap>> iterator = reusablePool.iterator();

        while (iterator.hasNext()) {
            Bitmap bitmap = iterator.next().get();
            if (bitmap != null) {
                if (checkInBitmap(bitmap, w, h, inSampleSize)) {
                    reusable = bitmap;
                    iterator.remove();
                    break;
                }
            } else {
                iterator.remove();
            }
        }

        return reusable;

    }

    /**
     * 校验bitmap 是否满足条件
     *
     * @param bitmap
     * @param w
     * @param h
     * @param inSampleSize
     * @return
     */
    private boolean checkInBitmap(Bitmap bitmap, int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getWidth() == w && bitmap.getHeight() == h && inSampleSize == 1;
        }
        if (inSampleSize > 1) {
            w /= inSampleSize;
            h /= inSampleSize;
        }
        int byteCount = w * h * getBytesPerPixel(bitmap.getConfig());
        // 图片内存 系统分配内存
        return byteCount <= bitmap.getAllocationByteCount();
    }

    /**
     * 通过像素格式计算每一个像素占用多少字节
     *
     * @param config
     * @return
     */
    private int getBytesPerPixel(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888) {
            return 4;
        }
        return 2;
    }

    public void shutDown() {
        shutDown = true;
    }

}