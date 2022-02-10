package com.ztiany.view.utils;

import android.graphics.PointF;

/**
 * 椭圆计算工具类
 */
public class OvalMath {

    private float mA;//长轴
    private float mB;//短轴

    private float mCenterX;//中心点X
    private float mCenterY;//中心点Y

    public OvalMath() {

    }

    /**
     * 初始化尺寸
     * @param a 长轴
     * @param b 短轴
     * @param centerX 椭圆中心x
     * @param centerY 椭圆中心y
     */
    public void initSize(float a, float b, float centerX, float centerY) {
        mA = a;
        mB = b;
        mCenterX = centerX;
        mCenterY = centerY;
    }

    /**
     * 根据角度算法椭圆边上的坐标
     *
     * @param angle  角度
     * @param pointF 用来存在坐标的PointF
     */
    public void calcPoint(float angle, PointF pointF) {
        float newX = (float) (Math.cos(Math.toRadians(angle)) * mA + mCenterX);
        float newY = (float) (Math.sin(Math.toRadians(angle)) * mB + mCenterY);
        pointF.set(newX, newY);
    }

    /**
     * 根据触摸的位置，计算角度触摸位置与椭圆中心的角度
     *
     * @param xTouch x点
     * @param yTouch y点
     * @return 角度
     */
    public float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mCenterX);
        double y = yTouch - (mCenterY);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);//Hypot求直角三角形的斜边长
    }

} 