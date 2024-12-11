package com.ztiany.view.courses.hencoderplus.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.RequiresApi;

public class SportView extends View {

    private static final float RING_WIDTH = UnitConverter.dpToPx(20);
    private static final float RADIUS = UnitConverter.dpToPx(150);
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHLIGHT_COLOR = Color.parseColor("#FF4081");

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect bounds = new Rect();
    Paint.FontMetrics metrics = new Paint.FontMetrics();

    public SportView(Context context) {
        super(context);
    }

    {
        paint.setTextSize(UnitConverter.dpToPx(100));
        //paint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(CIRCLE_COLOR);
        paint.setStrokeWidth(RING_WIDTH);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIUS, paint);

        // 绘制进度条
        paint.setColor(HIGHLIGHT_COLOR);
        paint.setStrokeCap(Paint.Cap.ROUND);

        canvas.drawArc(
                getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                -90,
                225,
                false, paint);

        paint.setStrokeCap(Paint.Cap.BUTT);

        // 绘制文字
        String text = "abab";
        paint.setStyle(Paint.Style.FILL);
        //paint.getTextBounds(text, 0, text.length(), bounds);
        //float offset = (bounds.top + bounds.bottom) /2f;
        paint.getFontMetrics(metrics);
        float offset = (metrics.ascent + metrics.descent) / 2f;
        canvas.drawText(text, getWidth() / 2, getHeight() / 2 - offset, paint);

        // 绘制文字：贴边
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(UnitConverter.dpToPx(150));
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, -bounds.left, -bounds.top, paint);

        paint.setTextSize(UnitConverter.dpToPx(15));
        paint.getTextBounds(text, 0, text.length(), bounds);
        canvas.drawText(text, -bounds.left, -bounds.top + paint.getFontSpacing(), paint);
    }

}