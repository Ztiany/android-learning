package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.util.AttributeSet;
import android.view.View;

public class DrawPictureView extends View {

    Picture mPicture;
    private PictureDrawable mDrawable;

    public DrawPictureView(Context context) {
        this(context,null);
    }

    public DrawPictureView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DrawPictureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }


    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mPicture = new Picture();

        Canvas canvas = mPicture.beginRecording(200, 100);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0x88FF0000);
        canvas.drawCircle(50, 50, 40, p);
        p.setColor(Color.GREEN);
        p.setTextSize(30);
        canvas.drawText("Pictures", 60, 60, p);
        mPicture.endRecording();
        mDrawable = new PictureDrawable(mPicture);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPicture(mPicture);

        canvas.drawPicture(mPicture, new RectF(0, 100, getWidth(), 200));//mPicture会被拉伸

        mDrawable.setBounds(0, 200, getWidth(), 300);//使用PictureDrawable，Picture不会被拉伸
        mDrawable.draw(canvas);

        mDrawable.setBounds(0, 300, 100, 400);
        mDrawable.draw(canvas);
    }
}
