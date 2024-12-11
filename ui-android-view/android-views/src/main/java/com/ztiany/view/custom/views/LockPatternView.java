package com.ztiany.view.custom.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static com.ztiany.view.custom.views.LockPatternView.Point.STATE_COMPLETE;
import static com.ztiany.view.custom.views.LockPatternView.Point.STATE_ERROR;
import static com.ztiany.view.custom.views.LockPatternView.Point.STATE_NORMAL;

/**
 * 手势锁
 */
public class LockPatternView extends View {

    public LockPatternView(Context context) {
        this(context, null);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mNormalPointColor = Color.BLUE;
        mErrorPointPointColor = Color.BLUE;
        mCompletePointColor = Color.BLUE;
        mAutoReset = false;
        mAutoResetTime = 2000;
        init();
        initPoints();
    }

    private int mWidth, mHeight;//宽高必须一致
    private int mOffsetX, mOffsetY;//偏移值
    private float mCircleRadius = 0.07f;
    private float mPathWidth = 0.01f;
    private boolean isStart, drawOutLine;
    private int mLastX, mLastY;
    private int mAutoResetTime;

    private String currentPassword;

    private boolean mModeInEdit = true;//true设置密码模式，false就是输入密码模式
    private boolean mAutoReset;

    private int mNormalPointColor;
    private int mErrorPointPointColor;
    private int mCompletePointColor;

    private Paint mPointPaint;
    private Paint mPathPaint;
    private Path mPath;
    private Point[][] mPoints;
    private List<Point> mSelectedPoints;

    private void init() {
        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(mNormalPointColor);
        mPath = new Path();
        mPathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathPaint.setStyle(Paint.Style.STROKE);//描边
        mPathPaint.setColor(mCompletePointColor);
        mPathPaint.setStrokeCap(Paint.Cap.ROUND);
        mPathPaint.setStrokeJoin(Paint.Join.ROUND);
        mSelectedPoints = new ArrayList<>();
    }

    private void initPoints() {
        mPoints = new Point[3][3];
        int temp = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                mPoints[i][j] = new Point(0, 0);
                mPoints[i][j].setIndex(temp++);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //保证宽高一致
        if (mWidth > mHeight) {//横屏模式
            mOffsetX = (mWidth - mHeight) / 2;
            mWidth = mHeight;
        } else {//竖屏
            mOffsetY = (mHeight - mWidth) / 2;
            mHeight = mWidth;
        }
        setPoint();
        calc();
    }

    private void setPoint() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 0) {
                    x = mOffsetX + mWidth / 4;
                } else if (i == 1) {
                    x = mOffsetX + mWidth / 2;
                } else if (i == 2) {
                    x = mOffsetX + mWidth - mWidth / 4;
                }
                if (j == 0) {
                    y = mOffsetY + mHeight / 4;
                } else if (j == 1) {
                    y = mOffsetY + mHeight / 2;
                } else if (j == 2) {
                    y = mOffsetY + mHeight - mHeight / 4;
                }
                mPoints[i][j].setXY(x, y);
            }
        }
    }

    private void calc() {
        mCircleRadius = mHeight * mCircleRadius;//半径
        mPathWidth = mHeight * mPathWidth;//线宽
        mPathPaint.setStrokeWidth(mPathWidth);
        mPointPaint.setStrokeWidth(mCircleRadius / 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoints(canvas);
        if (!isStart) {
            return;
        }
        drawLine(canvas);
    }

    private void drawLine(Canvas canvas) {
        mPath.reset();
        int temp = mSelectedPoints.size();
        Point point;
        for (int i = 0; i < temp; i++) {
            point = mSelectedPoints.get(i);
            if (i == 0) {
                mPath.moveTo(point.getX(), point.getY());
            }
            mPath.lineTo(point.getX(), point.getY());
        }
        if (drawOutLine) {
            mPath.lineTo(mLastX, mLastY);
        }
        canvas.drawPath(mPath, mPathPaint);
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                drawPoint(mPoints[i][j], canvas);
            }
        }
    }

    private void drawPoint(Point point, Canvas canvas) {
        int state = point.getState();
        mPointPaint.setStyle(Paint.Style.STROKE);
        switch (state) {
            case STATE_NORMAL:
                mPointPaint.setColor(mNormalPointColor);
                break;
            case STATE_COMPLETE:
                mPointPaint.setColor(mCompletePointColor);
                break;
            case STATE_ERROR:
                mPointPaint.setColor(mErrorPointPointColor);
                break;
        }
        canvas.drawCircle(point.getX(), point.getY(), mCircleRadius, mPointPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mLastX = (int) event.getX();
        mLastY = (int) event.getY();
        int action = event.getAction();
        Point point = null;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                resetAll();
                point = checkEventInPoints(event);
                if (point != null) {
                    point.setState(STATE_COMPLETE);
                    mSelectedPoints.add(point);
                    isStart = true;
                    drawOutLine = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isStart) {
                    point = checkEventInPoints(event);
                    if (point != null && !contains(point)) {
                        point.setState(STATE_COMPLETE);
                        mSelectedPoints.add(point);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                drawOutLine = false;
                if (mSelectedPoints.size() < 4) {
                    errorPotions();
                    if (mLockPatternListener != null) {
                        mLockPatternListener.onLittle();
                    }
                    checkRest();
                } else if (!mModeInEdit) {//输入密码验证模式
                    if (checkSelected()) {
                        if (mLockPatternListener != null) {
                            mLockPatternListener.onSuccess();
                        }
                    } else {
                        errorPotions();
                        if (mLockPatternListener != null) {
                            mLockPatternListener.onError();
                        }
                        checkRest();
                    }
                } else {
                    if (mLockPatternListener != null) {
                        mLockPatternListener.onGeneratePwd(generatePwd());
                    }

                }
                break;
        }
        invalidate();
        return isStart;
    }

    private void checkRest() {
        if (mAutoReset) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    reset();
                }
            }, mAutoResetTime);
        }
    }

    private String generatePwd() {
        StringBuilder sb = new StringBuilder();
        for (Point point : mSelectedPoints) {
            sb.append(point.getIndex());
        }
        return sb.toString();
    }

    private boolean checkSelected() {

        return generatePwd().equals(currentPassword);
    }

    private void errorPotions() {
        mPathPaint.setColor(mErrorPointPointColor);
        for (Point point : mSelectedPoints) {
            point.setState(STATE_ERROR);
        }
    }

    private boolean contains(Point point) {
        return mSelectedPoints.contains(point);
    }

    private void resetAll() {
        isStart = false;
        mPath.reset();
        mPathPaint.setColor(mCompletePointColor);
        mPointPaint.setColor(mNormalPointColor);
        mSelectedPoints.clear();
        for (Point[] pointArr : mPoints) {
            for (Point point : pointArr) {
                point.setState(STATE_NORMAL);
            }
        }
    }


    public Point checkEventInPoints(MotionEvent event) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (checkEvent(event, mPoints[i][j])) {
                    return mPoints[i][j];
                }
            }
        }
        return null;
    }

    private boolean checkEvent(MotionEvent event, Point point) {
        int x = Math.abs((int) event.getX()) - Math.abs(point.getX());
        int y = Math.abs((int) event.getY()) - Math.abs(point.getY());

        float distance = (float) Math.sqrt(x * x + y * y);

        Log.d("LockPatternView", "distance:" + distance + "   mCircleRadius = " + mCircleRadius);
        return distance < mCircleRadius;
    }


    /**
     * @param password
     */
    public void setInputMode(String password) {
        this.currentPassword = password;
        mModeInEdit = false;
    }


    private LockPatternListener mLockPatternListener;

    public void setLockPatternListener(LockPatternListener lockPatternListener) {
        this.mLockPatternListener = lockPatternListener;
    }

    public interface LockPatternListener {
        void onGeneratePwd(String string);

        void onError();

        void onLittle();

        void onSuccess();
    }

    public void reset() {
        resetAll();
        invalidate();
    }


    @IntDef(value = {STATE_ERROR, STATE_NORMAL, STATE_COMPLETE})
    @interface PointState {

    }

    static class Point {

        static final int STATE_NORMAL = 1;
        static final int STATE_ERROR = 2;
        static final int STATE_COMPLETE = 3;
        private int mState, mX, mY, index;

        int getState() {
            return mState;
        }

        void setState(@PointState int state) {
            mState = state;
        }

        int getIndex() {
            return index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        public int getX() {
            return mX;
        }

        public void setX(int x) {
            mX = x;
        }

        public int getY() {
            return mY;
        }

        public void setY(int y) {
            mY = y;
        }

        public Point() {
        }

        Point(int x, int y) {
            mX = x;
            mY = y;
            mState = STATE_NORMAL;
        }

        void setXY(int x, int y) {
            mX = x;
            mY = y;
        }

    }

}