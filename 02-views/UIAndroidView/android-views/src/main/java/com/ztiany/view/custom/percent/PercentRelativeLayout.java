package com.ztiany.view.custom.percent;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.ztiany.view.R;

public class PercentRelativeLayout extends RelativeLayout {

    public PercentRelativeLayout(Context context) {
        super(context);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

     @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         int widthSize = MeasureSpec.getSize(widthMeasureSpec);

         int heightSize = MeasureSpec.getSize(heightMeasureSpec);

         for (int i = 0; i < getChildCount(); i++) {

             View child = getChildAt(i);
             if (child.getLayoutParams() instanceof LayoutParams) {
                 LayoutParams layoutParams= (LayoutParams) child.getLayoutParams();
                 float widthPercent = layoutParams.widthPercent;
                 float heightPercent = layoutParams.heightPercent;
                 float marginLeftPercent = layoutParams.marginLeftPercent;
                 float marginRightPercent= layoutParams.marginRightPercent;
                 float marginTopPercent= layoutParams.marginTopPercent;
                 float marginBottomPercent = layoutParams.marginBottomPercent;

                 if (widthPercent > 0){
                     layoutParams.width = (int) (widthSize * widthPercent);
                 }

                 if (heightPercent > 0){
                     layoutParams.height = (int) (heightSize * heightPercent);
                 }

                 if (marginLeftPercent > 0){
                     layoutParams.leftMargin = (int) (widthSize * marginLeftPercent);
                 }

                 if (marginRightPercent > 0){
                     layoutParams.rightMargin = (int) (widthSize * marginRightPercent);
                 }

                 if (marginTopPercent > 0){
                     layoutParams.topMargin = (int) (heightSize * marginTopPercent);
                 }

                 if (marginBottomPercent > 0){
                     layoutParams.bottomMargin = (int) (heightSize * marginBottomPercent);
                 }
             }
         }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public RelativeLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private float widthPercent;
        private float heightPercent;
        private float marginLeftPercent;
        private float marginRightPercent;
        private float marginTopPercent;
        private float marginBottomPercent;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a= c.obtainStyledAttributes(attrs, R.styleable.PercentLayout);
            widthPercent = a.getFloat(R.styleable.PercentLayout_widthPercent, 0);
            heightPercent = a.getFloat(R.styleable.PercentLayout_heightPercent, 0);
            marginLeftPercent = a.getFloat(R.styleable.PercentLayout_marginLeftPercent, 0);
            marginRightPercent = a.getFloat(R.styleable.PercentLayout_marginRightPercent, 0);
            marginTopPercent = a.getFloat(R.styleable.PercentLayout_marginTopPercent, 0);
            marginBottomPercent = a.getFloat(R.styleable.PercentLayout_marginBottomPercent, 0);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }
    }

}