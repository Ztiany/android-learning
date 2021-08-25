package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice6DrawLineView extends View {

    private int mLineOffset;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public Practice6DrawLineView(Context context) {
        super(context);
    }

    public Practice6DrawLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice6DrawLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLineOffset = w / 8;
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStrokeWidth(10);
//        练习内容：使用 canvas.drawLine() 方法画直线
        canvas.drawLine(mCenterX - mLineOffset, mCenterY - mLineOffset, mCenterX + mLineOffset, mCenterY + mLineOffset, mPaint);
    }
}
