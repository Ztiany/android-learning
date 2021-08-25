package com.zero.giflibdemo.gif;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import java.io.InputStream;

public class GifFrame {

    static {
        System.loadLibrary("native-lib");
    }

    private int width;
    private int height;

    private int frameCount;

    long nativeHandle;//C++ 一帧的对象指针

    private GifFrame(long nativeHandle, int width, int height, int frameCount) {//JNI 创建java对象
        this.nativeHandle = nativeHandle;
        this.width = width;
        this.height = height;
        this.frameCount = frameCount;
    }

    public static GifFrame decodeStream(InputStream stream) {
        byte[] buffer = new byte[16 * 1024];
        return nativeDecodeStream(stream, buffer);
    }

    public static GifFrame decodeStream(final Context context, final String fileName) {
        return nativeDecodeStreamJNI((context == null) ? null : context.getAssets(), fileName);
    }

    private static native GifFrame nativeDecodeStream(InputStream stream, byte[] buffer);

    private static native GifFrame nativeDecodeStreamJNI(AssetManager assetManager, String gifPath);

    private native long nativeGetFrame(long nativeHandle, Bitmap bitmap, int frameIndex);

    public long getFrame(Bitmap bitmap, int frameIndex) {
        return nativeGetFrame(nativeHandle, bitmap, frameIndex);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFrameCount() {
        return frameCount;
    }

}
