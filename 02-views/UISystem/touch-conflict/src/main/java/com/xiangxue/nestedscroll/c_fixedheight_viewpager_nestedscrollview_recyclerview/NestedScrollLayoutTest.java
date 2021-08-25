package com.xiangxue.nestedscroll.c_fixedheight_viewpager_nestedscrollview_recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class NestedScrollLayoutTest extends NestedScrollView {
    private ViewGroup contentView;
    public NestedScrollLayoutTest(Context context) {
        super(context);
        init();
    }

    public NestedScrollLayoutTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedScrollLayoutTest(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NestedScrollLayoutTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 调整contentView的高度为父容器高度，使之填充布局，避免父容器滚动后出现空白
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup.LayoutParams lp = contentView.getLayoutParams();
        lp.height = getMeasuredHeight();
        contentView.setLayoutParams(lp);
    }
}
