package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SaveLayerView extends View {

    private Paint mPaint;
    private RectF mRectF;

    private Path mPath;

    public SaveLayerView(Context context) {
        this(context, null);
    }

    public SaveLayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SaveLayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mRectF = new RectF();

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int halfWidth = getMeasuredWidth() / 2;
        int halfHeight = getMeasuredHeight() / 2;

        mPaint.setColor(Color.BLUE);
        mRectF.set(halfWidth - 200, halfHeight - 200, halfWidth + 200, halfHeight + 200);
        canvas.drawRect(mRectF, mPaint);
        mRectF.set(halfWidth - 100, halfHeight - 100, halfWidth + 100, halfHeight + 100);

        int layer1 = canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG);

        canvas.rotate(15);
        mPaint.setColor(Color.RED);
        canvas.drawRect(mRectF, mPaint);


        canvas.restoreToCount(layer1);


        mRectF.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.saveLayerAlpha(mRectF, 0xAA, Canvas.ALL_SAVE_FLAG);//设置透明度为AA
        canvas.drawColor(Color.GREEN);
        canvas.restore();

        canvas.save();
        canvas.translate(halfWidth, halfHeight);
        mPath.addCircle(0, 0, halfHeight / 4, Path.Direction.CW);
        canvas.clipPath(mPath);
        canvas.drawColor(Color.GRAY);
        canvas.restore();

        mPaint.setColor(Color.RED);
        canvas.drawCircle(0, 0, halfWidth * 1.2F, mPaint);

    }
}
