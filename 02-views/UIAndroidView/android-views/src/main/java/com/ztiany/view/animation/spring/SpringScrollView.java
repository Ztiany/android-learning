package com.ztiany.view.animation.spring;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.dynamicanimation.animation.SpringAnimation;

/**
 * http://www.jianshu.com/p/ce6497cada9c
 * 经验：通过设置setTranslationY达到移动的效果
 */
public class SpringScrollView extends NestedScrollView {

    private float startDragY;
    private SpringAnimation springAnim;

    public SpringScrollView(Context context) {
        this(context, null);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringScrollView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        springAnim = new SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0);
        //刚度 默认1200 值越大回弹的速度越快
        springAnim.getSpring().setStiffness(800.0f);
        //阻尼 默认0.5 值越小，回弹之后来回的次数越多
        springAnim.getSpring().setDampingRatio(0.50f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() <= 0) {  //顶部下拉
                    //这里getScrollY始终==0
                    if (startDragY == 0) {//纪录初始位置
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY > 0) {//如果现在的手势位置大于刚刚的位置说明在下拉
                        setTranslationY((e.getRawY() - startDragY) / 3);//那就设置setTranslationY，整个移动ScrollView
                        return true;
                    } else {//否则就cancel掉，并且复位，接下来的事件交给ScrollView处理
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                } else if ((getScrollY() + getHeight()) >= getChildAt(0).getMeasuredHeight()) {  //底部上拉
                    if (startDragY == 0) {
                        startDragY = e.getRawY();
                    }
                    if (e.getRawY() - startDragY < 0) {
                        setTranslationY((e.getRawY() - startDragY) / 3);
                        return true;
                    } else {
                        springAnim.cancel();
                        setTranslationY(0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getTranslationY() != 0) {
                    springAnim.start();
                }
                startDragY = 0;
                break;
        }
        return super.onTouchEvent(e);
    }
}
