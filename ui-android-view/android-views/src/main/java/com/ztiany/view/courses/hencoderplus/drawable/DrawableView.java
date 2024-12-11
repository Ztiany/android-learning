package com.ztiany.view.courses.hencoderplus.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class DrawableView extends View {

    private Drawable drawable;

    public DrawableView(Context context) {
        super(context);
    }

    {
        drawable = new MeshDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.setBounds(100, 100, getWidth(), getHeight());
        drawable.draw(canvas);
    }

}