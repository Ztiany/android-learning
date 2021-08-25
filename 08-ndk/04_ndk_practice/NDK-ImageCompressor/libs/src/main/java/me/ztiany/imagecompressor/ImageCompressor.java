package me.ztiany.imagecompressor;

import android.graphics.Bitmap;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-11-15 23:36
 */
public class ImageCompressor {

    public static native void nativeCompress(Bitmap bitmap, int q, String path);

}
