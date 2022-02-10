package com.ztiany.recyclerview.swipemenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import com.ztiany.view.R;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-09-29 10:54
 */
public class SwipeMenuItem extends HorizontalScrollView {

    private View mMenu;
    private View mContent;

    private final int mMenuId;
    private final int mContentId;

    private int mMenuWidth;
    private boolean mIsMenuOpen = false;

    private final float mMenuPercent = 0.3F;

    public SwipeMenuItem(Context context) {
        this(context, null);
    }

    public SwipeMenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeMenuItem);
        mMenuId = typedArray.getResourceId(R.styleable.SwipeMenuItem_sm_menu_id, 0);
        mContentId = typedArray.getResourceId(R.styleable.SwipeMenuItem_sm_content_id, 0);
        typedArray.recycle();
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMenu = findViewById(mMenuId);
        mContent = findViewById(mContentId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mContent != null) {
            mContent.getLayoutParams().width = MeasureSpec.getSize(widthMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMenu != null) {
            mMenuWidth = mMenu.getMeasuredWidth();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mMenu == null || mContent == null) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int scrollX = getScrollX();
                if (mIsMenuOpen) {
                    if (mMenuWidth - scrollX > mMenuWidth * mMenuPercent) {
                        closeMenu();
                        return false;
                    } else {
                        openMenu();
                        return false;
                    }
                } else {
                    if (scrollX > mMenuWidth * mMenuPercent) {
                        openMenu();
                        return false;
                    } else {
                        closeMenu();
                        return false;
                    }
                }
        }
        return super.onTouchEvent(ev);
    }

    private void openMenu() {
        smoothScrollTo(mMenuWidth, 0);
        mIsMenuOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(0, 0);
        mIsMenuOpen = false;
    }

}
