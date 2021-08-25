package com.ztiany.view.utils;

import android.content.Context;
import android.graphics.PointF;

public class ViewMathUtils {


    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return UnitConverter.dpToPx(25);
    }

    /**
     * As meaning of method name. 获得两点之间的距离 (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2) 开平方
     * Math.sqrt:开平方 Math.pow(p0.y - p1.y, 2):求一个数的平方
     */
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        return (float) Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
    }

    /**
     * Get point between p1 and p2 by percent. 根据百分比获取两点之间的某个点坐标
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x, p2.x), evaluateValue(percent, p1.y, p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     */
    public static float evaluateValue(float fraction, Number start, Number end) {
        return start.floatValue() + (end.floatValue() - start.floatValue()) * fraction;
    }

    /**
     * Get the point of intersection between circle and line.
     * 获取通过指定圆心，斜率为lineK的直线与圆的交点。
     * <pre>
     *      圆圈方程：(x－a)²+(y－b)²=r²中，有三个参数a、b、r，即圆心坐标为（a，b），只要求出a、b、r，
     *      直线方程：y = kx  + b
     * </pre>
     *
     * @param pMiddle The circle center point.
     * @param radius  The circle radius.
     * @param lineK   The slope of line which cross the pMiddle.
     * @return 两个交点坐标
     */
    public static PointF[] getIntersectionPoints(PointF pMiddle, float radius, Double lineK) {
        PointF[] points = new PointF[2];

        //高中数学：几何
        float arcTan, xOffset, yOffset;
        if (lineK != null) {
            // 计算直角三角形边长
            // 余切函数（弧度）
            arcTan = (float) Math.atan(lineK);
            // 正弦函数
            xOffset = (float) (Math.sin(arcTan) * radius);
            // 余弦函数
            yOffset = (float) (Math.cos(arcTan) * radius);
        } else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
        points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);
        return points;
    }
}
