package com.hencoder.hencoderpracticedraw4.practice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice02ClipPathView extends View {
    Paint paint = new Paint();
    Bitmap bitmap;
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);
    Path mPath = new Path();

    public Practice02ClipPathView(Context context) {
        super(context);
    }

    public Practice02ClipPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02ClipPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPath.addCircle(point1.x + bitmap.getWidth()*0.65F, point2.y + bitmap.getHeight()*0.65F, bitmap.getWidth() * 0.65F, Path.Direction.CCW);
        canvas.clipPath(mPath);
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        mPath.reset();

        canvas.save();
        mPath.addCircle(point2.x + bitmap.getWidth()*0.65F + 20, point2.y + bitmap.getHeight()*0.65F + 20, bitmap.getWidth() * 0.65F, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }

}
