package com.ztiany.view.courses.hencoderplus.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * 圆盘
 */
public class DashBoardView extends View {

    private static final float RADIUS = UnitConverter.dpToPx(150);
    private static final float ANGLE = 120;
    private static final float LENGTH = UnitConverter.dpToPx(100);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path dash = new Path();
    PathDashPathEffect pathEffect;
    Path path;
    PathMeasure pathMeasure;

    public DashBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(UnitConverter.dpToPx(3));
        dash.addRect(0, 0, UnitConverter.dpToPx(2), UnitConverter.dpToPx(10), Path.Direction.CCW);
        path = new Path();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        path.addArc(
                getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE);

        pathMeasure = new PathMeasure(path, false);

        pathEffect = new PathDashPathEffect(
                dash,
                (pathMeasure.getLength() - UnitConverter.dpToPx(2)) / 20,
                0,
                PathDashPathEffect.Style.ROTATE);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画原图形
        canvas.drawArc(
                getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE,
                false, paint);

        // 画刻度
        paint.setPathEffect(pathEffect);
        canvas.drawArc(
                getWidth() / 2 - RADIUS,
                getHeight() / 2 - RADIUS,
                getWidth() / 2 + RADIUS,
                getHeight() / 2 + RADIUS,
                90 + ANGLE / 2,
                360 - ANGLE,
                false, paint);

        paint.setPathEffect(null);

        // 画指针
        canvas.drawLine(
                getWidth() / 2,
                getHeight() / 2,
                getWidth() / 2 + (float) Math.cos(Math.toRadians(getAngleForMark(5))) * LENGTH,
                getHeight() / 2 + (float) Math.sin(Math.toRadians(getAngleForMark(5))) * LENGTH,
                paint);
    }

    float getAngleForMark(int mark) {
        return 90 + ANGLE / 2 + (360 - ANGLE) / 20 * mark;
    }

}