package com.dongnao.dnskin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.dongnao.dnskin.R;
import com.dongnao.skin.core2.SkinViewSupport;
import com.dongnao.skin.core2.utils.SkinResources;

/**
 * @author Lance
 * @date 2018/3/12
 */

public class CircleView extends View implements SkinViewSupport {

    private AttributeSet attrs;
    //画笔
    private Paint mTextPain;
    //半径
    private int radius;


    private int corcleColorResId;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.attrs = attrs;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        corcleColorResId = typedArray.getResourceId(R.styleable.CircleView_corcleColor, 0);
        typedArray.recycle();
        mTextPain = new Paint();
        mTextPain.setColor(getResources().getColor(corcleColorResId));
        //开启抗锯齿，平滑文字和圆弧的边缘
        mTextPain.setAntiAlias(true);
        //设置文本位于相对于原点的中间
        mTextPain.setTextAlign(Paint.Align.CENTER);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取宽度一半
        int width = getWidth() / 2;
        //获取高度一半
        int height = getHeight() / 2;
        //设置半径为宽或者高的最小值
        radius = Math.min(width, height);
        //利用canvas画一个圆
        canvas.drawCircle(width, height, radius, mTextPain);

    }

    public void setCorcleColor(@ColorInt int color) {
        mTextPain.setColor(color);
        invalidate();
    }

    @Override
    public void applySkin() {
        if (corcleColorResId != 0) {
            int color = SkinResources.getInstance().getColor(corcleColorResId);
            setCorcleColor(color);
        }
    }


//    @Override
//    public void applySkin() {
//        if (corcleColorResId != 0) {
//            int color = SkinResources.getInstance().getColor(corcleColorResId);
//            setCorcleColor(color);
//        }
//    }
}
