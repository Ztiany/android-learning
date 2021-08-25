package com.ztiany.view.custom.views;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * todo
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-10 00:26
 */
public class ExtendViewUsefulMethodView extends View {

/*

# 继承View时可能用到的分发

    onFinishInflate
    onVisibilityChanged  当View被构造后，其默认会处于INVISIBLE不可见状态，随后被切换到VISIBLE可见状态，因此其在View被构造后会调用两次，
    onRtlPropertiesChanged 我们可以通过View的setLayoutDirection方法或者在AndroidManifest.xml中改变View或当前Activity默认的布局方向。
    onAttachedToWindow
    onWindowVisibilityChanged  View被添加到Window中后就会触发onWindowVisibilityChanged，此时则表示Window的显示状态发生了改变，Window由不可见转为了可见。实质上每当Window的显示状态发生改变后都会触发该方法。
    onMeasure
    onSizeChanged
    onLayout
    onWindowFocusChanged
    onDetachedFromWindow
    onDraw
    onKeyDown
    onKeyUp
    dispatchTouchEvent

    //Activity打开
    onFinishInflate() called
    onAttachedToWindow() called
    onWindowVisibilityChanged() called with: visibility = [0]
    onVisibilityChanged() called with: changedView = [com.ztiany.diffutil.CustomerVIew{d4e8be8 V.ED..... ......I. 0,0-0,0}], visibility = [0
    onMeasure() called with: widthMeasureSpec = [1073742099], heightMeasureSpec = [1073742099]
    onSizeChanged() called with: w = [275], h = [275], oldw = [0], oldh = [0]
    onLayout() called with: changed = [true], left = [402], top = [528], right = [677], bottom = [803]
    onDraw() called with: canvas = [android.view.DisplayListCanvas@e9372e7]
    onWindowFocusChanged() called with: hasWindowFocus = [true]
    onMeasure() called with: widthMeasureSpec = [1073742099], heightMeasureSpec = [1073742099]
    onLayout() called with: changed = [false], left = [402], top = [528], right = [677], bottom = [803]
    onDraw() called with: canvas = [android.view.DisplayListCanvas@e9372e7]

    //Activity停止
    onWindowFocusChanged() called with: hasWindowFocus = [false]
    09-10 19:45:26.103 2428-2428/com.ztiany D/CustomerVIew: onWindowVisibilityChanged() called with: visibility = [8]
    09-10 19:45:26.341 2428-2428/com.ztiany D/CustomerVIew: onVisibilityChanged() called with: changedView = [com.android.internal.policy.PhoneWindow$DecorView{effa062 I.E...... R......D 0,0-1080,1920}], visibility = [4]

    //Activity退出
    onWindowFocusChanged() called with: hasWindowFocus = [false]
    onWindowVisibilityChanged() called with: visibility = [8]
    onDetachedFromWindow() called
*/

    public ExtendViewUsefulMethodView(Context context) {
        super(context);
    }

    public ExtendViewUsefulMethodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtendViewUsefulMethodView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
