package com.ztiany.view.courses.hencoderplus.draw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.os.Build;
import android.view.View;

import com.ztiany.view.R;
import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.RequiresApi;

public class AvatarView2 extends View {

    private static final float WIDTH = UnitConverter.dpToPx(300);
    private static final float PADDING = UnitConverter.dpToPx(40);
    private static final float BORDER_WIDTH = UnitConverter.dpToPx(10);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap avatar;
    Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    RectF cut = new RectF();
    RectF border = new RectF();

    public AvatarView2(Context context) {
        super(context);
    }

    {
        avatar = getAvatar((int) WIDTH);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cut.set(PADDING, PADDING, PADDING + WIDTH, PADDING + WIDTH);

        border.set(
                PADDING - BORDER_WIDTH,
                PADDING - BORDER_WIDTH,
                PADDING + WIDTH + BORDER_WIDTH,
                PADDING + WIDTH + BORDER_WIDTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawOval(cut, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(avatar, PADDING, PADDING, paint);
        paint.setXfermode(null);
    }

    Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.img5, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.img5, options);
    }

}