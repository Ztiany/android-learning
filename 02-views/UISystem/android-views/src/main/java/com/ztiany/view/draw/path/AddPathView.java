package com.ztiany.view.draw.path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.utils.UnitConverter;

public class AddPathView extends View {

    private Path mPath;
    private Paint mPaint;
    private RectF mRectF;

    String mText = "A B C D E F G H I J K L M N O P Q R  S T U V W X Y Z";

    public AddPathView(Context context) {
        this(context, null);
    }

    public AddPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(UnitConverter.spToPx(18));
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mRectF.set(0, 0, 300, 300);
        mPath.lineTo(100,-100);
        mPath.arcTo(mRectF, 0, 270,true);
        mPath.lineTo(500, 500);
        canvas.drawPath(mPath, mPaint);
        canvas.drawTextOnPath(mText, mPath, 0, 0, mPaint);
    }
}
