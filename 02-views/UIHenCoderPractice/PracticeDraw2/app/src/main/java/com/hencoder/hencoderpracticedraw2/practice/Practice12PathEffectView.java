package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.SumPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice12PathEffectView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();
    private ComposePathEffect mComposePathEffect;
    private SumPathEffect mSumPathEffect;
    private PathDashPathEffect mPathDashPathEffect;
    private DashPathEffect mDashPathEffect;
    private DiscretePathEffect mDiscretePathEffect;
    private CornerPathEffect mCornerPathEffect;

    public Practice12PathEffectView(Context context) {
        super(context);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);

        path.moveTo(50, 100);
        path.rLineTo(50, 100);
        path.rLineTo(80, -150);
        path.rLineTo(100, 100);
        path.rLineTo(70, -120);
        path.rLineTo(150, 80);

        //radius 半径
        mCornerPathEffect = new CornerPathEffect(4);

        //segmentLength 段长
        //deviation 偏差
        mDiscretePathEffect = new DiscretePathEffect(4, 4);

        //intervals[] 间隔
        //phase  相差
        mDashPathEffect = new DashPathEffect(new float[]{3, 4, 5}, 4);

        //TRANSLATE：位移
        // ROTATE：旋转
        // MORPH：变体
        Path shape = new Path();
        shape.lineTo(20, 0);
        shape.lineTo(10, 20);
        shape.lineTo(0, 0);
        mPathDashPathEffect = new PathDashPathEffect(shape, 20, 20, PathDashPathEffect.Style.MORPH);

        //分别按照两种 PathEffect 分别对目标进行绘制
        mSumPathEffect = new SumPathEffect(mDashPathEffect, mDiscretePathEffect);

        //先对目标 Path 使用一个 PathEffect，然后再对这个改变后的 Path 使用另一个 PathEffect。
        //innerpe 是先应用的， outerpe 是后应用的。
        mComposePathEffect = new ComposePathEffect(mDashPathEffect, mDiscretePathEffect);

        //PathEffect 在有些情况下不支持硬件加速，需要关闭硬件加速才能正常使用：
        //Canvas.drawLine() 和 Canvas.drawLines() 方法画直线时，setPathEffect() 是不支持硬件加速的；
        //PathDashPathEffect 对硬件加速的支持也有问题，所以当使用 PathDashPathEffect 的时候，最好也把硬件加速关了。

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.setPathEffect() 来设置不同的 PathEffect

        // 第一处：CornerPathEffect
        paint.setPathEffect(mCornerPathEffect);
        canvas.drawPath(path, paint);

        canvas.save();
        canvas.translate(500, 0);
        // 第二处：DiscretePathEffect

        paint.setPathEffect(mDiscretePathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 200);
        // 第三处：DashPathEffect

        paint.setPathEffect(mDashPathEffect);
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 200);
        // 第四处：PathDashPathEffect

        paint.setPathEffect(mPathDashPathEffect);
        canvas.drawPath(this.path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 400);
        // 第五处：SumPathEffect

        paint.setPathEffect(mSumPathEffect);
        canvas.drawPath(this.path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 400);
        // 第六处：ComposePathEffect
        paint.setPathEffect(mComposePathEffect);
        canvas.drawPath(this.path, paint);
        canvas.restore();
    }
}
