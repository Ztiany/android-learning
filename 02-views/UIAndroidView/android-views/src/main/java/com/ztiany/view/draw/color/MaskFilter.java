package com.ztiany.view.draw.color;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.R;
import com.ztiany.view.utils.UnitConverter;


public class MaskFilter extends View {

    private Paint mPaint;
    private static final String TEXT = "MaskFilter 演示";
    private int mDistance;
    private Rect mRect;
    private Bitmap mBitmap;

    public MaskFilter(Context context) {
        this(context, null);
    }

    public MaskFilter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(UnitConverter.spToPx(16));
        mRect = new Rect();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDistance = h / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int dp20 = UnitConverter.dpToPx(20);
        mRect.set(dp20, dp20, dp20 * 4, dp20 * 4);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(mRect, mPaint);
        canvas.translate(0, mDistance);
        canvas.drawCircle(getMeasuredWidth() / 2, 0, mDistance / 2, mPaint);
        canvas.translate(0, mDistance);
        canvas.drawText(TEXT, dp20 * 5, 0, mPaint);
        canvas.translate(0, mDistance);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }


    /**
     * @param radius 模块半径
     * @param blur   模糊风格
     */
    public void setBlurMaskFilter(float radius, BlurMaskFilter.Blur blur) {
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius, blur);
        mPaint.setMaskFilter(blurMaskFilter);
        invalidate();
    }

    /**
     * @param direction
     * @param ambient
     * @param specular
     * @param blurRadius
     */
    public void setEmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius) {
        EmbossMaskFilter embossMaskFilter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
        mPaint.setMaskFilter(embossMaskFilter);
        invalidate();
    }

    public void noMaskFilter() {
        mPaint.setMaskFilter(null);
        invalidate();
    }
}
