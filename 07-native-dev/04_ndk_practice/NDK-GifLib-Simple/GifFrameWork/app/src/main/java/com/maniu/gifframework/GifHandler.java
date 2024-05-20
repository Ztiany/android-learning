package com.maniu.gifframework;

import android.graphics.Bitmap;

public class GifHandler {

    private final long gifHandler;

    static {
        System.loadLibrary("native-lib");
    }

    public int getWidth() {
        return getWidth(gifHandler);
    }

    public int getHeight() {
        return getHeight(gifHandler);
    }

    public int updateFrame(Bitmap bitmap) {
        return updateFrame(gifHandler, bitmap);
    }

    private GifHandler(long gifHandler) {
        this.gifHandler = gifHandler;
    }

    public static GifHandler load(String path) {
        long gifHandler = loadGif(path);
        return new GifHandler(gifHandler);
    }

    public static native long loadGif(String path);

    public native int getWidth(long gifHandler);

    public native int getHeight(long gifPoint);

    public native int updateFrame(long gifPoint, Bitmap bitmap);

}