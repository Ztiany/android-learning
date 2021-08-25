package com.ztiany.progress;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.ztiany.progress.imageloader.ProgressGlideModule;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-22 14:47
 */
@GlideModule
public class ConfigGlideModule extends ProgressGlideModule {

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        //内存缓存相关，默认是24m
        int memoryCacheSizeBytes = 1024 * 1024 * 20;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        //设置磁盘缓存及其路径
        int maxCacheSize = 100 * 1024 * 1024;
        String cacheFileName = "imgCache";
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, cacheFileName, maxCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
    }
}
