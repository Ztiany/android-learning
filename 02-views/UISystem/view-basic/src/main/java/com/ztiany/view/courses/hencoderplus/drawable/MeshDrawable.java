package com.ztiany.view.courses.hencoderplus.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeshDrawable extends Drawable {

    private static final int INTERVAL = UnitConverter.dpToPx(80);

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(UnitConverter.dpToPx(2));
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        for (int i = 0; i < getBounds().right; i += INTERVAL) {
            for (int j = 0; j < getBounds().bottom; j += INTERVAL) {
                canvas.drawLine(getBounds().left, j, getBounds().right, j, paint);
                canvas.drawLine(i, getBounds().top, i, getBounds().bottom, paint);
            }
        }
    }

    /*固定写法*/
    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    /*固定写法*/
    @Override
    public int getAlpha() {
        return paint.getAlpha();
    }

    /*固定写法*/
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    /*固定写法*/
    @Override
    public int getOpacity() {
        return paint.getAlpha() == 0 ? PixelFormat.TRANSPARENT :
                paint.getAlpha() == 0xff ? PixelFormat.OPAQUE : PixelFormat.TRANSLUCENT;
    }

}