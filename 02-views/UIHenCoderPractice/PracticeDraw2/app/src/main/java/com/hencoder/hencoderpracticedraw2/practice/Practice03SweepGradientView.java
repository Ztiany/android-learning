package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice03SweepGradientView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public Practice03SweepGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /*
        用 Paint.setShader(shader) 设置一个 SweepGradient
        SweepGradient 的参数：圆心坐标：(300, 300)；颜色：#E91E63 到 #2196F3
        */
        SweepGradient shader = new SweepGradient(300, 300, 0xFFE91E63, 0xFF2196F3);
        paint.setShader(shader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300, 300, 200, paint);
    }
}
