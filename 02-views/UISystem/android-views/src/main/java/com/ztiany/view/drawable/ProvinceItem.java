package com.ztiany.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;


/**
 * 省份的封装类
 */
public class ProvinceItem {

    //path对象
    private Path path;
    //绘制的颜色
    private int drawColor;

    public ProvinceItem(Path path) {
        this.path = path;
    }

    public void setDrawColor(int drawColor){
        this.drawColor = drawColor;
    }

    void drawItem(Canvas canvas, Paint paint,boolean isSelect){
        if(isSelect){
            //选中的时候
            paint.clearShadowLayer();
            paint.setStrokeWidth(2);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            paint.setShadowLayer(0,0,0,0xffffff);
            canvas.drawPath(path,paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawPath(path,paint);
        }else{
            //未选中的时候
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path,paint);


            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.BLACK);
            canvas.drawPath(path,paint);
        }
    }

    public boolean isTouch(float x,float y){
        //创建一个矩形
        RectF rectF = new RectF();
        //获取到当前省份的矩形边界
        path.computeBounds(rectF, true);
        //创建一个区域对象
        Region region = new Region();
        //将path对象放入到Region区域对象中
        region.setPath(path, new Region((int)rectF.left, (int)rectF.top,(int)rectF.right, (int)rectF.bottom));
        //返回是否这个区域包含传进来的坐标
        return region.contains((int)x,(int)y);
    }

}
