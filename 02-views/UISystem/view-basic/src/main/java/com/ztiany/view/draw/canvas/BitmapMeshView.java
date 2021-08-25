package com.ztiany.view.draw.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.view.R;



public class BitmapMeshView extends View {

    private final static int WIDTH_MESH = 19;
    private final static int HEIGHT_MESH = 19;
    private static final int COUNT = (WIDTH_MESH + 1) * (HEIGHT_MESH + 1);// 横纵向网格交织产生的点数量

    private float[] mVerts;

    private Bitmap mBitmap;// 位图资源

    public BitmapMeshView(Context context) {
        this(context, null);
    }

    public BitmapMeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapMeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mBitmap.getHeight(), MeasureSpec.EXACTLY));
    }

    private void init() {
        mVerts = new float[COUNT * 2];
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_scenery_01);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        int index = 0;
        float fx ;
        float fy ;
        for (int i = 0; i <= HEIGHT_MESH; i++) {
            fy = height * (i*1.0F / HEIGHT_MESH);
            for (int j = 0; j <= WIDTH_MESH; j++) {
                // 根据 x = x0 + b*y0公式
                fx = width * (j*1.0F / WIDTH_MESH) + 1 * fy;//这里1表示 tan45，即x方向倾斜45度
                setXYWithIndex(fx, fy, index);
                index++;
            }
        }
    }

    private void setXYWithIndex(float fx, float fy, int index) {
        mVerts[2 * index] = fx;
        mVerts[2 * index + 1] = fy;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmapMesh(mBitmap, WIDTH_MESH, HEIGHT_MESH, mVerts, 0, null, 0, null);
    }
}
