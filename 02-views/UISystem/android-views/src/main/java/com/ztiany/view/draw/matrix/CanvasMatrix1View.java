package com.ztiany.view.draw.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;

/**
 * 如何理解矩阵的变换顺序
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-11 10:48
 */
public class CanvasMatrix1View extends View {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CanvasMatrix1View(Context context) {
        super(context);
    }

    public CanvasMatrix1View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasMatrix1View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);

        canvas.save();
        canvas.drawLine(getMeasuredWidth() / 2F, 0, getMeasuredWidth() / 2F, getMeasuredHeight(), mPaint);
        canvas.drawLine(0, getMeasuredHeight() / 2F, getMeasuredWidth(), getMeasuredHeight() / 2F, mPaint);
        canvas.restore();


        //如果认为 Canvas 在每一次变换之后都形成了新的内部坐标体系，然后在下一次变换的时候以这个新的坐标体系为基准，那么就是正序的。
        //但这种思维方式不够直观，在多次变换的情况下会很难去计算参数。
        //此处的变换可以理解为：以原坐标为基准移动到绘制中心点，再以新的坐标为基准旋转30°
        canvas.save();
        canvas.translate(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F);
        canvas.rotate(10);
        canvas.drawRect(0, 0, 200, 200, mPaint);
        canvas.restore();


        //如果把所有的变换全部按照以原坐标体系为基准，就可以理解为倒序的。
        //此处的变换可以理解为：以原坐标为基准移动到绘制中心点，再以原坐标为基准旋转30°
        mPaint.setColor(Color.RED);
        canvas.save();
        canvas.rotate(10);
        canvas.drawLine(getMeasuredWidth() / 2F, 0, getMeasuredWidth() / 2F, getMeasuredHeight(), mPaint);
        canvas.drawLine(0, getMeasuredHeight() / 2F, getMeasuredWidth(), getMeasuredHeight() / 2F, mPaint);
        canvas.translate(getMeasuredWidth() / 2F, getMeasuredHeight() / 2F);
        canvas.drawRect(0, 0, 200, 200, mPaint);
        canvas.restore();
    }
}
