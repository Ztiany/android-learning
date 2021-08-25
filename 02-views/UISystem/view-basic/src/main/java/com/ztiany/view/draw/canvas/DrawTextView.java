package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;


public class DrawTextView extends View {

    private Paint mPaint;
    private Paint.FontMetrics mFontMetrics;

    private Path mPath;

    public DrawTextView(Context context) {
        this(context, null);
    }

    public DrawTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int textSize = UnitConverter.spToPx(20);
        mPaint.setTextSize(textSize);
        mFontMetrics = new Paint.FontMetrics();
        mPath = new Path();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTypeface(Typeface.SERIF);
        mPaint.setTextSize(UnitConverter.spToPx(18));

        canvas.drawColor(Color.BLACK);

        int halfHeight = getMeasuredHeight() / 2;
        canvas.drawLine(0, halfHeight, getMeasuredWidth(), halfHeight, mPaint);

        mPaint.setTextSize(UnitConverter.spToPx(10));
        mPaint.getFontMetrics(mFontMetrics);

        Log.d("DrawTextView", "mFontMetrics.ascent + mFontMetrics.descent:" + (mFontMetrics.ascent + mFontMetrics.descent));

        String text = "Canvas Draw Text yes  画布绘制文字";
        canvas.drawText(text, 0, (halfHeight - ((mFontMetrics.ascent + mFontMetrics.descent) / 2)), mPaint);

        //Direction 表示设置在使用path绘制文本时的位置
        // Path.Direction.CCW表示在path的外围
        //  Path.Direction.CW表示在path的内围
        mPath.addCircle((float) (getMeasuredWidth() / 1.5), getMeasuredHeight() / 2, getMeasuredHeight() / 4, Path.Direction.CCW);

        String pathText = "Canvas Draw Text yes  onPath , Canvas Draw Text yes  onPath , Canvas Draw Text yes  onPath , Canvas Draw Text yes  onPath";
        canvas.drawTextOnPath(pathText, mPath, 0, 0, mPaint);
    }
}
