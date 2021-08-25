package com.ztiany.view.custom.message_drag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ztiany.view.R;
import com.ztiany.view.utils.ViewMathUtils;
import com.ztiany.view.utils.ViewUtils;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-15 11:25
 */
class MessageDragListener implements View.OnTouchListener, MessageBubbleView.OnMessageBubbleViewDragListener {


    private final View mOriginalView;
    private final OnViewDragDisappearListener mOnViewDragDisappearListener;
    private final WindowManager.LayoutParams mParams;
    private Context mContext;

    private FrameLayout mBombLayout;//View消失时的爆炸效果布局
    private ImageView mBombView;//爆炸的效果
    private final MessageBubbleView mMessageBubbleView;
    private final WindowManager mWindowManager;

    MessageDragListener(View originalView, OnViewDragDisappearListener onViewDragDisappearListener) {
        mOriginalView = originalView;
        mOnViewDragDisappearListener = onViewDragDisappearListener;
        mContext = originalView.getContext();

        mMessageBubbleView = new MessageBubbleView(mContext);
        mMessageBubbleView.setOnMessageBubbleViewDragListener(this);

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        mParams.format = PixelFormat.TRANSPARENT; //背景要透明.否则会黑屏

        mBombLayout = new FrameLayout(mContext);
        mBombView = new ImageView(mContext);
        mBombLayout.addView(mBombView);
        mBombView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 搞一个原始View的快照,并添加WindowManger中
                mWindowManager.addView(mMessageBubbleView, mParams);

                // 初始化贝塞尔View的中心点 在原始View的中线点
                // 获取原始View在屏幕中的坐标
                int[] location = new int[2];
                mOriginalView.getLocationOnScreen(location); //默认获取的是View左上角在屏幕上的坐标(y坐标包含状态栏的高度)
                mMessageBubbleView.initPoint(location[0] + mOriginalView.getWidth() / 2, location[1] + mOriginalView.getHeight() / 2 - ViewMathUtils.getStatusBarHeight(mContext));

                // 给贝塞尔View一张原始View的快照
                Bitmap copyBitmap = ViewUtils.getBitmapFromView(mOriginalView);
                // 给拖拽的消息设置一张原始View的快照
                mMessageBubbleView.setDragBitmap(copyBitmap);

                break;
            case MotionEvent.ACTION_MOVE:
                // 解决一点击View出现闪动的bug(一旦拖动在隐藏)
                if (mOriginalView.getVisibility() == View.VISIBLE) {
                    mOriginalView.setVisibility(View.INVISIBLE);
                }
                //更新拖拽的位置
                mMessageBubbleView.updateDragPointer(event.getRawX(), event.getRawY() - ViewMathUtils.getStatusBarHeight(mContext)); // 同样要减去状态栏高度

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMessageBubbleView.handleActionUP();
                break;
        }
        return true;
    }

    @Override
    public void onViewDragDisappear(PointF dragPoint) {
        // 移除消息气泡贝塞尔View,同时添加一个爆炸的View动画(帧动画)
        mWindowManager.removeView(mMessageBubbleView);
        mWindowManager.addView(mBombLayout, mParams);
        mBombView.setBackgroundResource(R.drawable.anim_bubble_bomb);

        AnimationDrawable bombDrawable = (AnimationDrawable) mBombView.getBackground();
        // 矫正爆炸时,位置偏下的问题
        mBombView.setX(dragPoint.x - bombDrawable.getIntrinsicWidth() / 2);
        mBombView.setY(dragPoint.y - bombDrawable.getIntrinsicHeight() / 2);
        bombDrawable.start();

        mBombView.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 动画执行完毕,把爆炸布局及时从WindowManager移除
                mWindowManager.removeView(mBombLayout);
                if (mOnViewDragDisappearListener != null) {
                    mOnViewDragDisappearListener.onDisappear(mOriginalView);
                }
            }
        }, ViewUtils.getAnimationTotalTime(bombDrawable));
    }

    /**
     * 松手收，View又回到原理的的位置
     */
    @Override
    public void onViewDragRestore() {
        mWindowManager.removeView(mMessageBubbleView);
        mOriginalView.setVisibility(View.VISIBLE);
    }

    /**
     * 用户处理View的消失的监听
     */
    public interface OnViewDragDisappearListener {
        /**
         * 原始View消失的监听
         *
         * @param originalView 原始的View
         */
        void onDisappear(View originalView);
    }
}
