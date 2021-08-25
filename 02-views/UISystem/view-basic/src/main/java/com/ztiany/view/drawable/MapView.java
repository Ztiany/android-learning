package com.ztiany.view.drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ztiany.view.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.Nullable;


public class MapView extends View {

    private Context context;
    private float scale = 1.0f;
    private RectF totalRect;
    private int[] colorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    private List<ProvinceItem> itemList;
    private Paint paint;
    private ProvinceItem select;

    public MapView(Context context) {
        super(context);
    }

    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleTouch(event.getX(), event.getY());
        return super.onTouchEvent(event);
    }

    private void handleTouch(float x, float y) {
        if (itemList == null) {
            return;
        }
        ProvinceItem selectItem = null;
        for (ProvinceItem provinceItem : itemList) {
            if (provinceItem.isTouch(x / scale, y / scale)) {
                selectItem = provinceItem;
            }
        }
        if (selectItem != null) {
            select = selectItem;
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        获取到当前控件宽高值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

//        map 的宽度  和高度
        if (totalRect != null) {
            double mapWidth = totalRect.width();
            scale = (float) (width / mapWidth);
        }

        setMeasuredDimension(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    private void init(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        loadThread.start();
    }

    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (itemList != null) {
            canvas.save();
            canvas.scale(scale, scale);
            for (ProvinceItem provinceItem : itemList) {
                if (provinceItem != select) {
                    provinceItem.drawItem(canvas, paint, false);
                }
            }
            if (select != null) {
                select.drawItem(canvas, paint, true);
            }
        }

    }

    private Thread loadThread = new Thread() {
        @Override
        public void run() {
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);
            List<ProvinceItem> list = new ArrayList<>();
            try {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例
                DocumentBuilder builder = null; //从factory获取DocumentBuilder实例
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(inputStream);   //解析输入流 得到Document实例
                Element rootElement = doc.getDocumentElement();
                NodeList items = rootElement.getElementsByTagName("path");
                //  中国地图的  矩形
                float left = -1;
                float right = -1;
                float top = -1;
                float bottom = -1;
                for (int i = 0; i < items.getLength(); i++) {
                    Element element = (Element) items.item(i);
                    String pathData = element.getAttribute("android:pathData");
                    Path path = PathParser.createPathFromPathData(pathData);
                    ProvinceItem provinceItem = new ProvinceItem(path);
                    list.add(provinceItem);
                    //                    获取宽高
                    RectF rect = new RectF();
                    path.computeBounds(rect, true);
                    left = left == -1 ? rect.left : Math.min(left, rect.left);
                    right = right == -1 ? rect.right : Math.max(right, rect.right);
                    top = top == -1 ? rect.top : Math.min(top, rect.top);
                    bottom = bottom == -1 ? rect.bottom : Math.max(bottom, rect.bottom);
                    totalRect = new RectF(left, top, right, bottom);
                }
                itemList = list;
                handler.sendEmptyMessage(1);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (itemList == null) {
                    return;
                }
                int totalNumber = itemList.size();
                for (int i = 0; i < totalNumber; i++) {
                    int color = Color.WHITE;
                    int flag = i % 4;
                    switch (flag) {
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
                    itemList.get(i).setDrawColor(color);
                }
                postInvalidate();
            }
        };
    };

}
