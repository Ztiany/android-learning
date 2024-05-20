package com.ztiany.view.custom.message_drag;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.ztiany.view.utils.UnitConverter;
import com.ztiany.view.utils.ViewMathUtils;

/**
 * http://www.jianshu.com/p/36ad239ba001
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-14 23:30
 */
public class MessageBubbleView extends View {

    // 两个实心圆--根据点的坐标来绘制圆
    private PointF mDragPoint, mFixationPoint;
    private Paint mPaint;
    private int mDragRadius = 9; // 拖拽圆半径

    // 固定圆最大半径(初始半径)/半径的最小值
    private int mFixationRadiusMax = 7;
    private int mFixationRadiusMin = 3;
    private int mFixationRadius;

    private OnMessageBubbleViewDragListener mDragListener;

    private Bitmap mDragBitmap;//OriginView的快照

    public MessageBubbleView(Context context) {
        this(context, null);
    }

    public MessageBubbleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        mDragRadius = UnitConverter.dpToPx(mDragRadius);
        mFixationRadiusMax = UnitConverter.dpToPx(mFixationRadiusMax);
        mFixationRadiusMin = UnitConverter.dpToPx(mFixationRadiusMin);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mFixationPoint == null || mDragPoint == null) {
            return;
        }
        // 画两个圆: 固定圆有一个初始化大小,而且随着两圆距离的增大而变小,小到一定程度就不见了(不画了)
        canvas.drawCircle(mDragPoint.x, mDragPoint.y, mDragRadius, mPaint);

        // 拖拽View的快照 大小不变,位置跟随手指移动

        Path path = getPath();
        if (path != null) {
            canvas.drawCircle(mFixationPoint.x, mFixationPoint.y, mFixationRadius, mPaint);
            canvas.drawPath(path, mPaint);
        }

        //画被拖拽的View的快照
        if (mDragBitmap != null) {
            canvas.drawBitmap(mDragBitmap, mDragPoint.x - mDragBitmap.getWidth() / 2, mDragPoint.y - mDragBitmap.getHeight() / 2, null); // 从左上角开始绘制
        }
    }

    private Path getPath() {

        double distance = ViewMathUtils.getDistanceBetween2Points(mDragPoint, mFixationPoint);

        // 随着拖拽的距离变化,不断改变固定圆的半径
        mFixationRadius = (int) (mFixationRadiusMax - distance / 16);

        if (mFixationRadius < mFixationRadiusMin) {
            // 超过一定距离  贝塞尔曲线和固定圆都不要绘制了
            return null;
        }

        Path path = new Path();

        // 求角a
        double angleA = Math.atan((mDragPoint.y - mFixationPoint.y) / (mDragPoint.x - mFixationPoint.x));//atan即根据值求角度

        float P0x = (float) (mFixationPoint.x + mFixationRadius * Math.sin(angleA));
        float P0y = (float) (mFixationPoint.y - mFixationRadius * Math.cos(angleA));

        float P3x = (float) (mFixationPoint.x - mFixationRadius * Math.sin(angleA));
        float P3y = (float) (mFixationPoint.y + mFixationRadius * Math.cos(angleA));

        float P1x = (float) (mDragPoint.x + mDragRadius * Math.sin(angleA));
        float P1y = (float) (mDragPoint.y - mDragRadius * Math.cos(angleA));

        float P2x = (float) (mDragPoint.x - mDragRadius * Math.sin(angleA));
        float P2y = (float) (mDragPoint.y + mDragRadius * Math.cos(angleA));

        // 拼接 贝塞尔曲线路径
        // 移动到我们的起始点,否则默认从(0,0)开始
        path.moveTo(P0x, P0y);
        // 求控制点坐标,我们取两圆圆心为控制点(如果取黄金比例0.618是比较好的)
        PointF controlPoint = getControlPoint();
        // 画第一条 前两个参数为控制点坐标  后两个参数为终点坐标
        path.quadTo(controlPoint.x, controlPoint.y, P1x, P1y);
        path.lineTo(P2x, P2y);
        path.quadTo(controlPoint.x, controlPoint.y, P3x, P3y);
        path.close();
        return path;
    }

    /**
     * 获取控制点，取两点的中心点
     */
    private PointF getControlPoint() {
        return new PointF((mDragPoint.x + mFixationPoint.x) / 2, (mDragPoint.y + mFixationPoint.y) / 2);
    }

    public static void bindToView(View view, MessageDragListener.OnViewDragDisappearListener onViewDragDisappearListener) {
        view.setOnTouchListener(new MessageDragListener(view, onViewDragDisappearListener));
    }

    public void setOnMessageBubbleViewDragListener(MessageDragListener onMessageBubbleViewDragListener) {
        mDragListener = onMessageBubbleViewDragListener;
    }

    public void initPoint(int x, int y) {
        mDragPoint = new PointF(x, y);
        mFixationPoint = new PointF(x, y);
    }

    public void updateDragPointer(float x, float y) {
        mDragPoint.set(x, y);
        invalidate();
    }

    public void setDragBitmap(Bitmap dragBitmap) {
        mDragBitmap = dragBitmap;
    }

    /**
     * 处理手势抬起
     */
    public void handleActionUP() {
        if (mFixationRadius < mFixationRadiusMin) {//如果固定的圆的半径效果与最小半径就显式爆炸效果
            // 显示爆炸效果
            if (mDragListener != null) {
                mDragListener.onViewDragDisappear(mDragPoint);
            }
            return;
        }

        // 显示回弹效果 属性动画
        ValueAnimator animator = ObjectAnimator.ofFloat(1f);
        animator.setDuration(300);

        final PointF startPoint = new PointF(mDragPoint.x, mDragPoint.y);
        final PointF endPoint = new PointF(mFixationPoint.x, mFixationPoint.y);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                //PointF pointF = ViewMathUtils.getPointByPercent(mDragPoint,mFixationPoint,percent);
                // 这个起始点和结束点,不能这样传值(这样传值的话,起点和终点一直在发生变化),应该转的是固定点和刚开始放手时的坐标的点
                PointF pointF = ViewMathUtils.getPointByPercent(startPoint, endPoint, percent);
                updateDragPointer(pointF.x, pointF.y);
            }
        });
        animator.setInterpolator(new OvershootInterpolator(4f));
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 当动画结束的时候,重新让View可拖动
                if (mDragListener != null) {
                    mDragListener.onViewDragRestore();
                }
            }
        });
    }


    public interface OnMessageBubbleViewDragListener {

        /**
         * 松手，View消失
         */
        void onViewDragDisappear(PointF pointF);

        /**
         * 松手，View回到原来的位置
         */
        void onViewDragRestore();
    }
}
