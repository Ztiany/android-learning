package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class Practice10HistogramView extends View {

    private final float mTextTop;
    private String[] mItems = {"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
    private float[] mItemsPercent = {0.1F, 0.3F, 0.3F, 0.5F, 0.8F, 0.9F, 0.4F};

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int mLeft;
    private int mBottom;

    private int height;
    private int width;

    private int margin;
    private float mRectWidth;
    private RectF mRectF = new RectF();

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mTextPaint.setColor(Color.WHITE);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();

        mTextTop = -fontMetrics.top;
        mRectPaint.setColor(Color.GREEN);

        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));

        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLeft = (int) (w * 0.1F);
        width = (int) (w * 0.8F);

        mBottom = (int) (h * 0.9F);
        height = (int) (h * 0.8F);

        int rectTotalWidth = width - (margin * 8);
        mRectWidth = rectTotalWidth / 7F;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        canvas.drawLine(mLeft, mBottom, mLeft, mBottom - height, mLinePaint);
        canvas.drawLine(mLeft, mBottom, mLeft + width, mBottom, mLinePaint);

        int start = mLeft;
        float textWidth;
        float baseLine = mBottom + mTextTop;
        for (int i = 0; i < mItemsPercent.length; i++) {
            start += margin;

            mRectF.set(start, mBottom - height * mItemsPercent[i], start + mRectWidth, mBottom);

            textWidth = mTextPaint.measureText(mItems[i]);
            canvas.drawText(mItems[i], start + (mRectWidth / 2 - textWidth / 2), baseLine, mTextPaint);

            start += mRectWidth;
            canvas.drawRect(mRectF, mRectPaint);
        }
    }
}
