package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice9DrawPathView extends View {

    private Path path = new Path();
    private Path path2 = new Path();
    private Path path3 = new Path();
    //两个一样大小的正方形
    private RectF rectF = new RectF(20, 20, 200, 200);
    private RectF rectF1 = new RectF(200, 20, 380, 200);
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public Practice9DrawPathView(Context context) {
        super(context);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice9DrawPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        因为画发是扇形而不是弧形
        canvas.drawArc(rectF, 135, 225, true, mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawArc(rectF1, 180, 225, true, mPaint);


//        练习内容：使用 canvas.drawPath() 方法画心形
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mPaint.setColor(Color.BLUE);
        path.addArc(rectF, 135, 225);//
        path.arcTo(rectF1, 180, 225, false);
        path.lineTo(200, 300);
        canvas.drawPath(path, mPaint);
        path.close();
        canvas.drawPath(path, mPaint);

        //原理1，因为画笔的风格是填充，所以绘制的时候会连接起点和终点
        canvas.translate(0, -getMeasuredHeight() / 2);
        mPaint.setColor(Color.BLACK);
        path2.addArc(rectF, 135, 225);//
        path2.arcTo(rectF1, 180, 225);
        canvas.drawPath(path2, mPaint);

        //原理2，因为是抬了一笔，所以路径的起点发生了改变，所以形状是不饱满的
        canvas.translate(-getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        path3.addArc(rectF, 135, 225);//
        path3.arcTo(rectF1, 180, 225, true);
        canvas.drawPath(path3, mPaint);
    }
}
