package com.ztiany.view.custom.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ring extends View {

    private int[] colors = {Color.BLACK, Color.CYAN, Color.DKGRAY, Color.GREEN, Color.RED, Color.MAGENTA, Color.YELLOW, Color.LTGRAY};

    private List<Circle> cs;
    private boolean isDrawing;// 是否还在画
    private Random random;
    private int minStep = 17;
    private Paint paint;// 画笔

    public Ring(Context context, AttributeSet attrs) {
        super(context, attrs);
        random = new Random();
        cs = new ArrayList<>();
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                add(event);
            case MotionEvent.ACTION_MOVE:
                add(event);
                break;
        }
        return true;
    }

    private void add(MotionEvent event) {
        int cx = (int) event.getX();
        int cy = (int) event.getY();
        addCircle(cx, cy);
    }

    private void addCircle(int cx, int cy) {
        if (cs.size() == 0) {// 只有在第一次添加圆圈的时候 才发送消息开始花圈
            addCircleToList(cx, cy);
            handler.sendEmptyMessage(0);
            isDrawing = true;
        } else {
            if (Math.abs(cx - cs.get(cs.size() - 1).cx) > minStep
                    || Math.abs(cy - cs.get(cs.size() - 1).cy) > minStep) {
                addCircleToList(cx, cy);
            }
        }
    }

    private void addCircleToList(int cx, int cy) {
        Circle circle = new Circle();
        paint = new Paint();
        paint.setColor(colors[random.nextInt(8)]);
        paint.setAntiAlias(true);// 抗锯齿
        paint.setStyle(Style.STROKE);// 空心圆圈;
        paint.setAlpha(255);// 设置透明度
        circle.cx = cx;
        circle.cy = cy;
        circle.p = paint;
        cs.add(circle);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            flushState();
            invalidate();
            if (isDrawing) {
                sendEmptyMessageDelayed(0, 100);
            }
        }
    };

    private void flushState() {// 刷新
        Circle circle;
        for (int i = 0; i < cs.size(); i++) {
            circle = cs.get(i);
            circle.radius += 10;// 半径
            circle.p.setStrokeWidth(circle.radius / 5);// 线宽
            int nextAlpha = circle.p.getAlpha() - 15;
            if (nextAlpha <= 20) {
                nextAlpha = 0;
            }
            circle.p.setAlpha(nextAlpha);
            if (circle.p.getAlpha() == 0) {
                cs.remove(circle);
            }
            if (cs.size() == 0) {
                isDrawing = false;
            }
        }
    }

    // 开始测量
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // 开始绘画 每一次绘画界重置
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < cs.size(); i++) {
            Circle c = cs.get(i);
            canvas.drawCircle(c.cx, c.cy, c.radius, c.p);
        }
        super.onDraw(canvas);
    }

    private static class Circle {
        Paint p;// 画笔
        int cx;// 圆心坐标
        int cy;//
        float radius;// 半径
    }

}