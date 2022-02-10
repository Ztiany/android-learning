package com.ztiany.view.custom.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ztiany.view.R;


public class RulerWheel extends View {

    // 默认刻度模式
    public static final int MOD_TYPE_SCALE = 5;
    // 1/2模式
    public static final int MOD_TYPE_HALF = 2;
    public static final int MOD_TYPE_TEN_ONE = 10;

    //刻度的上对齐方式
    public static final int ALIGN_MOD_UP = 0;
    //刻度的下对齐方式
    public static final int ALIGN_MOD_DOWN = 1;
    //默认的对其方式
    public static final int DEFAULT_ALIGN_MOD = ALIGN_MOD_DOWN;

    private int alignMode = DEFAULT_ALIGN_MOD;

    //最大刻度的粗细
    private int mMaxBarWidth;
    private int mMidBarWidth;
    private int mMinBarWidth;

    // 分隔线(大号)
    private int mLineHeightMax;
    private float mLineHeightPercentMax;
    private int mLineColorMax;

    // 分隔线(中号)
    private int mLineHeightMid;
    private float mLineHeightPercentMid;
    private int mLineColorMid;

    // 分隔线(小号)
    private int mLineHeightMin;
    private float mLineHeightPercentMin;
    private int mLineColorMin;

    // 当前值
    private int mCurrValue;
    // 显示最大值
    private int mMaxValue;
    //显示最小值
    private int mMinValue;

    private static final int DEFAULT_MINI_VALUE = 0;//默认最小值
    private static final int DEFAULT_MAX_VALUE = 100;//默认最大值
    private static final int DEFAULT_CUR_VALUE = 0;//默认当前值
    private static final int DEFAULT_TEXT_SIZE = 14;//默认文字大小sp
    private static final int DEFAULT_LINE_SIZE = 4;//默认线宽px

    private static final int DEFAULT_LINE_PERCENT_MAX = 3;//默认粗线比例
    private static final int DEFAULT_LINE_PERCENT_MID = 4;//默认中线比例
    private static final int DEFAULT_LINE_PERCENT_MIN = 7;//默认细线比例

    private static final int DEFAULT_LINE_DIVIDER_SIZE = 0;

    // 分隔模式
    private int mModType = MOD_TYPE_SCALE;
    /**
     * 分隔线之间间隔
     */
    private int mLineDivider;
    // 滚动器
    private WheelHorizontalScroller scroller;
    // 是否执行滚动
    private boolean isScrollingPerformed;
    // 滚动偏移量
    private int scrollingOffset;
    // 显示刻度值
    private boolean isShowScaleValue;

    /**
     * 是否支持渐显效果
     */
    private boolean mIsGradient = false;//梯度
    private boolean mIsScaleValueGradient = false;//梯度

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private int mTextHeight;
    private Paint.FontMetrics mFontMetrics;

    //Touch Event---------------------
    private float mDownFocusX;
    private float mDownFocusY;
    private boolean isDisallowIntercept;
    private OnWheelScrollListener onWheelListener;


    public RulerWheel(Context context) {
        this(context, null);
    }

    public RulerWheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerWheel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        scroller = new WheelHorizontalScroller(context, scrollingListener);
        // 获取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RulerWheel);

        // 刻度宽度
        int scaleWidth = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_scaleWidth, DEFAULT_LINE_SIZE);
        linePaint.setStrokeWidth(scaleWidth);

        // 刻度颜色
        mLineColorMax = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMax, Color.BLACK);
        mLineColorMid = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMid, Color.BLACK);
        mLineColorMin = mTypedArray.getColor(R.styleable.RulerWheel_lineColorMin, Color.BLACK);

        //线的高度
        mLineHeightPercentMax = mTypedArray.getFloat(R.styleable.RulerWheel_MaxScaleHeightPercent, DEFAULT_LINE_PERCENT_MAX);
        mLineHeightPercentMid = mTypedArray.getFloat(R.styleable.RulerWheel_MaxScaleHeightPercent, DEFAULT_LINE_PERCENT_MID);
        mLineHeightPercentMin = mTypedArray.getFloat(R.styleable.RulerWheel_MaxScaleHeightPercent, DEFAULT_LINE_PERCENT_MIN);

        int textSize = (int) mTypedArray.getDimension(R.styleable.RulerWheel_text_Size,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_TEXT_SIZE, getResources().getDisplayMetrics()));

        mCurrValue = mTypedArray.getInteger(R.styleable.RulerWheel_def_value, DEFAULT_CUR_VALUE);
        mMaxValue = mTypedArray.getInteger(R.styleable.RulerWheel_max_value, DEFAULT_MAX_VALUE);
        mMinValue = mTypedArray.getInteger(R.styleable.RulerWheel_min_value, DEFAULT_MINI_VALUE);

        if (mCurrValue < mMinValue) {
            mCurrValue = mMinValue;
        }

        // 刻度模式
        mModType = obtainMode(mTypedArray.getInteger(R.styleable.RulerWheel_mode, 0));

        // 刻度对齐模式
        alignMode = mTypedArray.getInteger(R.styleable.RulerWheel_alignMode, DEFAULT_ALIGN_MOD);

        // 线条间距
        mLineDivider = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_line_divider, DEFAULT_LINE_DIVIDER_SIZE);

        // 显示刻度值
        isShowScaleValue = mTypedArray.getBoolean(R.styleable.RulerWheel_showScaleValue, true);
        //Text
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        mFontMetrics = textPaint.getFontMetrics();
        mTextHeight = (int) (mFontMetrics.bottom - mFontMetrics.top + 0.5F);

        //---1/14---新加的特性----
        mMaxBarWidth = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_MaxBarSize, scaleWidth);
        mMidBarWidth = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_MidBarSize, scaleWidth);
        mMinBarWidth = mTypedArray.getDimensionPixelSize(R.styleable.RulerWheel_MinBarSize, scaleWidth);

        //渐显显示刻度
        mIsGradient = mTypedArray.getBoolean(R.styleable.RulerWheel_showGradient, false);
        mIsScaleValueGradient = mTypedArray.getBoolean(R.styleable.RulerWheel_scaleValueGradient, false);
        int textColor = mTypedArray.getColor(R.styleable.RulerWheel_text_color, Color.BLACK);
        textPaint.setColor(textColor);

        mTypedArray.recycle();
    }

    private int obtainMode(int mode) {
        if (mode == 1) {
            return MOD_TYPE_HALF;
        }
        return MOD_TYPE_SCALE;
    }

    // Scrolling listener
    WheelHorizontalScroller.ScrollingListener scrollingListener = new WheelHorizontalScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);
        }

        @Override
        public void onFinished() {
            if (thatExceed()) {//是否超过
                return;
            }
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }
            scrollingOffset = 0;
            invalidate();
        }

        @Override
        public void onJustify() {//对齐
            if (thatExceed()) {//超过
                return;
            }
            if (Math.abs(scrollingOffset) > WheelHorizontalScroller.MIN_DELTA_FOR_SCROLLING) {
                if (scrollingOffset < -mLineDivider / 2) {//分割线之间的间隔
                    scroller.scroll(mLineDivider + scrollingOffset, 0);
                } else if (scrollingOffset > mLineDivider / 2) {
                    scroller.scroll(scrollingOffset - mLineDivider, 0);
                } else {
                    scroller.scroll(scrollingOffset, 0);
                }
            }
        }
    };

    private void doScroll(int delta) {
        //-> delta <0
        //<- delta >0
        scrollingOffset += delta;
        int offsetCount = scrollingOffset / mLineDivider;//计算格子
        if (0 != offsetCount) {//超过一个格子
            mCurrValue -= offsetCount;//当前值减去滑动的格子数
            scrollingOffset -= offsetCount * mLineDivider;//计算了格子之后，重新减去消耗的偏移值
            if (null != onWheelListener) {
                //回调通知最新的值
                int valueIndex = Math.min(Math.max(mMinValue, mCurrValue), mMaxValue);
                onWheelListener.onChanged(this, valueIndex);
            }
        }
        invalidate();
    }

    /**
     * 越界回滚
     */
    private boolean thatExceed() {
        //这个是越界后需要回滚的大小值
        int outRange = 0;
        if (mCurrValue < mMinValue) {
            outRange = (mCurrValue - mMinValue) * mLineDivider;
        } else if (mCurrValue > mMaxValue) {
            outRange = (mCurrValue - mMaxValue) * mLineDivider;
        }
        if (0 != outRange) {
            scrollingOffset = 0;
            scroller.scroll(-outRange, 300);
            return true;
        }
        return false;
    }

    public void setValues(int minValue, int maxValue, int current, boolean trigger) {
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("minValue >= maxValue");
        }
        mMinValue = minValue;
        mMaxValue = maxValue;
        mCurrValue = current;
        if (mCurrValue < mMinValue) {
            mCurrValue = mMinValue;
        }
        if (mCurrValue > mMaxValue) {
            mCurrValue = mMaxValue;
        }
        if (trigger) {
            onWheelListener.onScrollingFinished(this);
        }
        invalidate();
    }

    public void setValue(int current, boolean trigger) {
        mCurrValue = current;
        if (mCurrValue < mMinValue) {
            mCurrValue = mMinValue;
        }
        if (mCurrValue > mMaxValue) {
            mCurrValue = mMaxValue;
        }
        if (trigger) {
            onWheelListener.onScrollingFinished(this);
        }
        invalidate();
    }

    /**
     * 获取当前值
     */
    public int getValue() {
        return Math.min(Math.max(mMinValue, mCurrValue), mMaxValue);
    }

    /***
     * 如果想改各个刻度的高度值可以修改这里面的三个参数的值
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == 0 || h == 0)
            return;
        int rHeight = h - getPaddingTop() - getPaddingBottom();
        mLineHeightMax = (int) (rHeight / mLineHeightPercentMax + 0.5F);
        mLineHeightMid = (int) (rHeight / mLineHeightPercentMid + 0.5F);
        mLineHeightMin = (int) (rHeight / mLineHeightPercentMin + 0.5F);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int rWidth = getWidth() - getPaddingLeft() - getPaddingRight();//相对宽度
        int rHeight = getHeight();//相对高度
        //画数据
        drawRulerLine(canvas, rWidth, rHeight);
        //画指示器
        drawMiddleUpArrowLine(canvas, rWidth, rHeight);
    }

    /**
     * 画中间的箭头指示的，当然也可以替代别的mask
     */
    private void drawMiddleUpArrowLine(Canvas canvas, int rWidth, int rHeight) {
        int centerX = rWidth / 2;
        canvas.drawLine(centerX, 0, centerX, rHeight - mTextHeight, mMarkPaint);
    }

    /**
     * @param rWidth  显示宽度
     * @param rHeight 显示高度
     */
    private void drawRulerLine(Canvas canvas, int rWidth, int rHeight) {
        // 根据间隔计算当前一半宽度的个数+多画2个
        final int halfCount = (int) Math.ceil(rWidth / 2f / mLineDivider) + 2;//？为什么要偏移两个，应该只是为了多画两个，防止有少画的可能
        final int distanceX = scrollingOffset;//当前偏移值
        final int currValue = mCurrValue;
        drawDownMode(canvas, halfCount, distanceX, currValue, rWidth, rHeight);
    }

    //标刻的对齐方式是下对齐
    private void drawDownMode(Canvas canvas, int halfCount, int distanceX, int currValue, int rWidth, int rHeight) {
        int value;
        float xPosition;

        for (int i = 0; i < halfCount; i++) {
            //画显示在屏幕上的右半部分数据---right part
            xPosition = rWidth / 2f + i * mLineDivider + distanceX;//这里上了distanceX，即偏移值
            value = currValue + i;

            if (xPosition <= rWidth && value >= mMinValue && value <= mMaxValue) {//在范围内才绘制(小于view宽度、大于最小值，小于最大值)
                //draw start
                if (mModType == MOD_TYPE_HALF) {
                    drawHalfMode(canvas, rHeight, halfCount, xPosition, value, i);
                } else if (mModType == MOD_TYPE_SCALE) {
                    drawScaleMode(canvas, rHeight, halfCount, xPosition, value, i);
                } else if (mModType == MOD_TYPE_TEN_ONE) {
                    drawTen_OneMode(canvas, rHeight, halfCount, xPosition, value, i);
                }
            }
            //画显示在屏幕上的左半部分数据---left part
            xPosition = rWidth / 2f - i * mLineDivider + distanceX;
            value = currValue - i;
            if (xPosition > getPaddingLeft() && value >= mMinValue && value <= mMaxValue) {
                if (mModType == MOD_TYPE_HALF) {
                    drawHalfMode(canvas, rHeight, halfCount, xPosition, value, i);
                } else if (mModType == MOD_TYPE_SCALE) {
                    drawScaleMode(canvas, rHeight, halfCount, xPosition, value, i);
                } else if (mModType == MOD_TYPE_TEN_ONE) {
                    drawTen_OneMode(canvas, rHeight, halfCount, xPosition, value, i);
                }
                //----draw end
            }
        }
    }

    private void drawTen_OneMode(Canvas canvas, int rHeight, int halfCount, float xPosition, int value, int i) {
        if (value % MOD_TYPE_TEN_ONE == 0) {
            drawMaxLine(canvas, rHeight, halfCount, xPosition, value, i);
        } else {
            drawMinLine(canvas, rHeight, halfCount, xPosition, i);
        }
    }

    private void drawHalfMode(Canvas canvas, int rHeight, int halfCount, float xPosition, int value, int i) {
        if (value % MOD_TYPE_HALF == 0) {//高格子 模式有 5 或者 2
            drawMaxLine(canvas, rHeight, halfCount, xPosition, value, i);
        } else {
            drawMidLine(canvas, rHeight, halfCount, xPosition, i);
        }
    }

    private void drawScaleMode(Canvas canvas, int rHeight, int halfCount, float xPosition, int value, int i) {
        if (value % MOD_TYPE_SCALE == 0) {
            if (value % (MOD_TYPE_SCALE * 2) == 0) {//十个格子画一个长线
                drawMaxLine(canvas, rHeight, halfCount, xPosition, value, i);
            } else {
                drawMidLine(canvas, rHeight, halfCount, xPosition, i);
            }
        } else {
            drawMinLine(canvas, rHeight, halfCount, xPosition, i);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // real Draw
    ///////////////////////////////////////////////////////////////////////////

    private void drawMaxLine(Canvas canvas, int rHeight, int halfCount, float xPosition, int value, int i) {
        linePaint.setColor(mLineColorMax);
        linePaint.setStrokeWidth(mMaxBarWidth);
        linePaint.setAlpha(getAlpha(halfCount, i));
        float ryMax = getStartLineY(rHeight, mLineHeightMax);
        canvas.drawLine(xPosition, ryMax, xPosition, ryMax + mLineHeightMax, linePaint);
        if (isShowScaleValue) {
            if (mIsScaleValueGradient) {
                textPaint.setAlpha(getAlpha(halfCount, i));
            }
            canvas.drawText(String.valueOf(value), xPosition, getTextBaseLine(rHeight, mLineHeightMax), textPaint);
        }
    }

    private void drawMinLine(Canvas canvas, int rHeight, int halfCount, float xPosition, int i) {
        float ryMid = getStartLineY(rHeight, mLineHeightMin);
        linePaint.setColor(mLineColorMin);
        linePaint.setStrokeWidth(mMinBarWidth);
        linePaint.setAlpha(getAlpha(halfCount, i));
        canvas.drawLine(xPosition, ryMid, xPosition, ryMid + mLineHeightMin, linePaint);
    }

    private void drawMidLine(Canvas canvas, int rHeight, int halfCount, float xPosition, int i) {
        float ryMid = getStartLineY(rHeight, mLineHeightMid);

        linePaint.setStrokeWidth(mMidBarWidth);
        linePaint.setColor(mLineColorMid);
        linePaint.setAlpha(getAlpha(halfCount, i));
        canvas.drawLine(xPosition, ryMid, xPosition, ryMid + mLineHeightMid, linePaint);
    }

    private float getTextBaseLine(int rHeight, int lineHeight) {
        if (alignMode == ALIGN_MOD_UP) {
            return getPaddingTop() + lineHeight - mFontMetrics.top;
        } else {
            return rHeight - getPaddingBottom() - mFontMetrics.bottom;
        }
    }

    private float getStartLineY(int rHeight, int lineHeight) {
        if (alignMode == ALIGN_MOD_UP) {
            return getPaddingTop() + mTextHeight;
        } else {
            return rHeight - getPaddingBottom() - mTextHeight - lineHeight;
        }
    }

    private int getAlpha(int halfCount, int index) {
        if (mIsGradient) {
            int MAX_ALPHA_VALUE = 255;
            return MAX_ALPHA_VALUE / halfCount * (halfCount - index);
        } else {
            return 255;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownFocusX = event.getX();
                mDownFocusY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDisallowIntercept && Math.abs(event.getY() - mDownFocusY) < Math.abs(event.getX() - mDownFocusX)) {
                    isDisallowIntercept = true;
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                isDisallowIntercept = false;
                break;
        }
        return scroller.onTouchEvent(event);
    }

    public void setModType(int modType) {
        mModType = modType;
        invalidate();
    }

    public void setScrollingListener(OnWheelScrollListener listener) {
        onWheelListener = listener;
    }

    private void notifyScrollingListenersAboutStart() {
        if (null != onWheelListener) {
            onWheelListener.onScrollingStarted(this);
        }
    }

    private void notifyScrollingListenersAboutEnd() {
        if (null != onWheelListener) {
            onWheelListener.onScrollingFinished(this);
        }
    }

    @SuppressWarnings("all")
    public interface OnWheelScrollListener {

        void onChanged(RulerWheel wheel, int value);

        void onScrollingStarted(RulerWheel wheel);

        void onScrollingFinished(RulerWheel wheel);
    }

    public static class OnWheelScrollListenerAdapter implements OnWheelScrollListener {

        public void onChanged(RulerWheel wheel, int value) {}

        public void onScrollingStarted(RulerWheel wheel) {}

        public void onScrollingFinished(RulerWheel wheel) {}
    }

}