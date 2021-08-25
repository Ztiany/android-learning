package com.ztiany.view.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ztiany.view.R;
import com.ztiany.view.bitmap.cache.ImageCache;
import com.ztiany.view.bitmap.cache.ImageResize;
import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class BitmapAdapter extends RecyclerView.Adapter<BitmapAdapter.BitmapViewHolder> {

    private static final String TAG = "BitmapAdapter";

    private Context context;

    public BitmapAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BitmapViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = new AppCompatImageView(viewGroup.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UnitConverter.dpToPx(100)));
        return new BitmapViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BitmapViewHolder bitmapViewHolder, int i) {

        // 原始方法获取bitmap
        //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_mv_w);

        // 第一种优化
        //Bitmap bitmap = ImageResize.resizeBitmap(context, R.drawable.icon_mv, 80, 80, false);

        // 第二种优化
        Bitmap bitmap = ImageCache.getInstance().getBitmapFromMemory(String.valueOf(i));

        Log.e(TAG, "使用内存缓存" + bitmap);
        if (bitmap == null) {

            Bitmap reusable = ImageCache.getInstance().getReusable(60, 60, 1);
            Log.e(TAG, "使用复用缓存" + reusable);

            bitmap = ImageCache.getInstance().getBitmapFromDisk(String.valueOf(i), reusable);
            Log.e(TAG, "使用磁盘缓存" + reusable);

            if (bitmap == null) {
                // 网络获取
                bitmap = ImageResize.resizeBitmap(context, R.drawable.img_girl_01, 80, 80, false, reusable);
                //放入内存
                ImageCache.getInstance().putBitmap2Memory(String.valueOf(i), bitmap);
                //放入磁盘
                ImageCache.getInstance().putBitmap2Disk(String.valueOf(i), bitmap);
            }
        }

        bitmapViewHolder.iv.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return 1000;
    }

    class BitmapViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv;

        BitmapViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView;
        }
    }

}
