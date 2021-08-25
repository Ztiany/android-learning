package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice01LinearGradientView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Practice01LinearGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        /*
         用 Paint.setShader(shader) 设置一个 LinearGradient
         LinearGradient 的参数：坐标：(100, 100) 到 (500, 500) ；颜色：#E91E63 到 #2196F3

         x0 y0 x1 y1：渐变的两个端点的位置
        color0 color1 是端点的颜色
         tile：端点范围之外的着色规则
         */
        LinearGradient shader = new LinearGradient(100, 100, 500, 500, 0xFFE91E63, 0xFF2196F3, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300, 300, 200, paint);
    }
}
