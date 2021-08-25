package com.ztiany.view.draw.path;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 17:08
 */
public class PathMeasureTanView extends View {

    private Path mPath;
    private float[] pos;
    private float[] tan;
    private Paint mPaint;
    float currentValue = 0;
    private PathMeasure mMeasure;

    public PathMeasureTanView(Context context) {
        this(context, null);
    }

    public PathMeasureTanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mMeasure = new PathMeasure();
        mPath.addCircle(0, 0, 200, Path.Direction.CW);
        mMeasure.setPath(mPath, false);
        pos = new float[2];
        tan = new float[2];
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        currentValue = (float) valueAnimator.getAnimatedValue();
                        invalidate();
                    }
                });
                animator.setDuration(3000);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.start();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMeasure.getPosTan(mMeasure.getLength() * currentValue, pos, tan);

        float degrees = (float) (Math.toDegrees(Math.atan2(tan[1], tan[0])));//得到角度

        canvas.save();
        canvas.translate(400, 400);//移动400
        canvas.drawPath(mPath, mPaint);//大圆圈
        canvas.drawCircle(pos[0], pos[1], 10, mPaint);//运动轨迹小圆圈
        canvas.rotate(degrees);//旋转画布
        canvas.drawLine(0, -200, 300, -200, mPaint);
        canvas.restore();
    }


}
