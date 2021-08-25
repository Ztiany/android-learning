package com.maniu.mn_vip_svg_map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.core.graphics.PathParser;

public class MapView extends View {

    //上下文
    private Context context;
    //画笔
    private Paint paint;
    //适配比例
    private float scale = 1.0f;
    //矩形对象
    private RectF totalRect;
    //定义一个装载所有省份的集合
    List<ProviceItem> itemList;
    //绘制地图的颜色
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    //是否XML已经解析完毕
    private boolean isEnd = false;
    //当前选中的省份
    private ProviceItem select;


    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        loadThread.start();

    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Thread loadThread = new Thread(new Runnable() {
        @Override
        public void run() {
            //定义一个输入流对象去加载xml文件
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);
            //定义一个装载所有省份的集合
            List<ProviceItem> list = new ArrayList<>();
            try {
                //获取到解析器的工厂类
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                //获取到SVG图形的XML对象
                Document parse = builder.parse(inputStream);
                //获取都节点目录
                Element documentElement = parse.getDocumentElement();
                //通过Element获取到所有path节点的集合
                NodeList items = documentElement.getElementsByTagName("path");
                //定义四个点
                float left= -1;
                float bottom= -1;
                float top= -1;
                float right = -1;
                //遍历所有的path节点
                for(int x=0;x<items.getLength();x++){
                    //获取到每一个path节点
                    Element element = (Element) items.item(x);
                    String pathData = element.getAttribute("android:pathData");
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProviceItem proviceItem = new ProviceItem(path);
                    list.add(proviceItem);

                    //初始化每个省份的矩形
                    RectF rect = new RectF();
                    //获取到每个省份的边界
                    path.computeBounds(rect,true);

                    //获取到left最小的值
                    left = left == -1?rect.left:Math.min(left,rect.left);
                    //获取right最大值
                    right = right==-1?rect.right:Math.max(right,rect.right);
                    //遍历取出每个path中的top取所有的最小值
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    //遍历取出每个path中的bottom取所有的最大值
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                }
                //封装地图的矩形
                totalRect = new RectF(left,top,right,bottom);
                itemList = list;
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });


    /**
     * 设置省份的颜色
     */
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(itemList == null || itemList.size()<=0){
                return;
            }
            int size = itemList.size();
            for(int x=0;x<size;x++){
                int color = Color.WHITE;
                int flag = x % 4;
                switch (flag){
                    case 1:
                        color = colorArray[0];
                        break;
                    case 2:
                        color = colorArray[1];
                        break;
                    case 3:
                        color = colorArray[2];
                        break;
                    default:
                        color = Color.CYAN;
                        break;
                }
                itemList.get(x).setDrawColor(color);
            }
            isEnd = true;
            measure(getMeasuredWidth(),getMeasuredHeight());
            //调用绘制
            postInvalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isEnd){
            return;
        }
        if(itemList !=null && itemList.size()>0){
            canvas.save();
            canvas.scale(scale,scale);
            for (ProviceItem proviceItem : itemList) {
                if(select == proviceItem){
                    proviceItem.drawItem(canvas,paint,true);
                }else{
                    proviceItem.drawItem(canvas,paint,false);
                }
            }
        }
        super.onDraw(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取到当前控件宽高值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(!isEnd){
            setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height , MeasureSpec.EXACTLY));
            return;
        }
        if(totalRect !=null){
            //获取到地图的矩形的宽度
            double mapWidth = totalRect.width();
            //获取到比例值
            scale = (float) (width/mapWidth);
        }
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height , MeasureSpec.EXACTLY));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //将当前手指触摸到位置传过去  判断当前点击的区域
        handlerTouch(event.getX(),event.getY());
        return super.onTouchEvent(event);
    }

    /**
     * 判断区域
     * @param x
     * @param y
     */
    private void handlerTouch(float x, float y) {
        //判空
        if(itemList ==null || itemList.size() ==0){
            return;
        }
        //定义一个空的被选中的省份
        ProviceItem selectItem =null;
        for (ProviceItem proviceItem : itemList) {
            //入股点击的是这个省份的范围之内 就把当前省份的封装对象绘制的方法 传一个true
            if(proviceItem.isTouch(x/scale,y/scale)){
                selectItem = proviceItem;
            }
        }
        if(selectItem !=null){
            select = selectItem;
            postInvalidate();
        }
    }

}
