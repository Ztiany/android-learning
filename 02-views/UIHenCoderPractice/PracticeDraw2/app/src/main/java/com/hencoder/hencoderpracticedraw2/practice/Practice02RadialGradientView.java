package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice02RadialGradientView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Practice02RadialGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        /*
        用 Paint.setShader(shader) 设置一个 RadialGradient
        RadialGradient 的参数：圆心坐标：(300, 300)；半径：200；颜色：#E91E63 到 #2196F3

        centerX centerY：辐射中心的坐标
        radius：辐射半径
        centerColor：辐射中心的颜色
        edgeColor：辐射边缘的颜色
        tileMode：辐射范围之外的着色模式
         */
        RadialGradient shader = new RadialGradient(300, 300, 200, 0xFFE91E63, 0xFF2196F3, Shader.TileMode.REPEAT);
        paint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(300, 300, 200, paint);
    }
}
