package com.ztiany.view.custom.flow_layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private List<Line> mChildrenList = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthResult, heightResult;

        int childCount = getChildCount();
        int maxLineWidth = 0;
        int singleLineHeight = 0;
        int lineHeight = 0;
        int lineWidth = 0;

        View child;
        MarginLayoutParams mlp;
        for (int i = 0; i < childCount; i++) {

            child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            //测量孩子
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            mlp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + mlp.rightMargin + mlp.leftMargin;
            int childHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

            if (lineWidth + childWidth > widthSize - getPaddingLeft() - getPaddingRight()) {//要换行

                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineWidth = 0;

                lineHeight += singleLineHeight;
                singleLineHeight = 0;

                singleLineHeight = Math.max(singleLineHeight, childHeight);
                lineWidth += childWidth;

            } else {//不换行
                singleLineHeight = Math.max(singleLineHeight, childHeight);
                lineWidth += childWidth;
            }

            if (i == childCount - 1) {
                maxLineWidth = Math.max(maxLineWidth, lineWidth);
                lineHeight += singleLineHeight;
            }
        }

        if (widthMode == MeasureSpec.EXACTLY) {
            widthResult = widthSize;
        } else {
            widthResult = lineWidth + getPaddingRight() + getPaddingLeft();
            if (widthMode == MeasureSpec.AT_MOST) {
                widthResult = Math.min(widthSize, widthResult);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            heightResult = heightSize;
        } else {
            heightResult = lineHeight + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                heightResult = Math.min(heightResult, heightSize);
            }
        }

        Log.d("FlowLayout", "widhtResult:" + widthResult);
        Log.d("FlowLayout", "heightResult:" + heightResult);

        setMeasuredDimension(widthResult, heightResult);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("FlowLayout", "l:" + l);

        mChildrenList.clear();
        int childCount = getChildCount();
        int lineHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int lineWidth = getWidth() - getPaddingLeft() - getPaddingRight();

        int tempLineWidth = 0;
        int tempLineHeight = 0;

        View child;
        MarginLayoutParams mlp;
        @SuppressLint("DrawAllocation") Line line = new Line();

        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
            mlp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + mlp.rightMargin + mlp.leftMargin;
            int childHeight = child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin;

            if (tempLineWidth + childWidth > lineWidth) {//要换行了

                line.lineHeight = tempLineHeight;
                mChildrenList.add(line);
                line = new Line();
                tempLineWidth = 0;

                Log.d("FlowLayout", "换行 i:" + i);
                line.addChild(child);
                tempLineWidth += childWidth;
                tempLineHeight = Math.max(tempLineHeight, childHeight);

            } else {
                Log.d("FlowLayout", "不换行 i:" + i);
                line.addChild(child);
                tempLineWidth += childWidth;
                tempLineHeight = Math.max(tempLineHeight, childHeight);
            }

            if (i == childCount - 1) {
                line.lineHeight = tempLineHeight;
                mChildrenList.add(line);
            }
        }

        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (Line theLine : mChildrenList) {

            for (View view : theLine.mViews) {
                mlp = (MarginLayoutParams) view.getLayoutParams();

                int cLeft = left + mlp.leftMargin;
                int cRight = cLeft + view.getMeasuredWidth();
                int cTop = top + mlp.topMargin;
                int cBottom = cTop + view.getMeasuredHeight();

                Log.d("FlowLayout", String.format("l = %d   t =  %d  r = %d b = %d", cLeft, cTop, cRight, cBottom));

                left += view.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin;
                view.layout(cLeft, cTop, cRight, cBottom);
            }
            left = getPaddingLeft();
            top += theLine.lineHeight;
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    private class Line {

        List<View> mViews = new ArrayList<>();
        int lineHeight;

        void addChild(View view) {
            mViews.add(view);
        }
    }

}
