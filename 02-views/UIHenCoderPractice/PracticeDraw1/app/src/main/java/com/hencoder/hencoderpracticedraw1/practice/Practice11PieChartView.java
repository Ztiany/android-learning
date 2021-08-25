package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice11PieChartView extends View {


    private float[] mItemsPercent = {0.03F, 0.03F, 0.04F, 0.15F, 0.25F, 0.35F, 0.15F};

    private int[] mItemsColor = {0xFF1E80F0, 0xFF118575, 0xFF8c8c8c, 0xFF830A9B, Color.BLUE, Color.GREEN, Color.RED};
    private RectF mRectF = new RectF();
    private Paint mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Practice11PieChartView(Context context) {
        super(context);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice11PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float width = h * 0.6F;
        float left = w * 0.2F;
        float top = h * 0.2F;

        mRectF.set(left, top, left + width, top + width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画饼图
        float startAngle = 0;
        float sweepAngle;
        for (int i = 0; i < mItemsColor.length; i++) {
            mArcPaint.setColor(mItemsColor[i]);
            sweepAngle = 360 * mItemsPercent[i];
            if (i > 0) {
                float start = startAngle + 1.5F;
                float sweep = sweepAngle - 1.5F;
                if (i == 5) {
                    float[] offset = calc(start, sweep);
                    canvas.translate(offset[0], offset[1]);
                    canvas.drawArc(mRectF, start, sweep, true, mArcPaint);
                    canvas.translate(-offset[0], -offset[1]);
                } else {
                    canvas.drawArc(mRectF, start, sweep, true, mArcPaint);
                }
            } else {
                canvas.drawArc(mRectF, startAngle, sweepAngle, true, mArcPaint);
            }
            startAngle += sweepAngle;
        }
    }

    //计算应该偏移的值
    private float[] calc(float start, float sweep) {
        float angle = start + (sweep / 2);
        int quadrant = getQuadrant(angle);
        float smallAngle = angle % 90;
        float offset = 10;
        if (quadrant != 0) {
            double radians = Math.toRadians(smallAngle);
            float x = (float) (offset * Math.cos(radians));
            float y = (float) (offset * Math.sin(radians));
            if (quadrant == 1) {
                y = -y;
            } else if (quadrant == 2) {
                y = -y;
                x = -x;
            } else if (quadrant == 3) {
                x = -x;
            }
            return new float[]{x, y};
        } else {//正好在轴上
            if (smallAngle == 0) {
                return new float[]{offset, 0};
            } else if (smallAngle == 90) {
                return new float[]{0, offset};
            } else if (smallAngle == 180) {
                return new float[]{-offset, 0};
            } else {
                return new float[]{0, -offset};
            }
        }
    }

    //假设只存在正角度
    private int getQuadrant(float angle) {
        angle = angle % 360;
        if (angle > 0 && angle < 90) {
            return 4;
        } else if (angle > 90 && angle < 180) {
            return 3;
        } else if (angle > 180 && angle < 270) {
            return 2;
        } else if (angle > 270 && angle < 360) {
            return 1;
        } else {
            return 0;
        }
    }
}
