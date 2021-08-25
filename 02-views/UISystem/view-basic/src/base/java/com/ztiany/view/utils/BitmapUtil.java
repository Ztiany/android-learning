package com.ztiany.view.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2021-03-04 11:53
 */
public class BitmapUtil {

    /**
     * You can use extension function toBitmap defined in kit-library instead.
     *
     * @see <a href='https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap'>how-to-convert-a-drawable-to-a-bitmap</>
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
