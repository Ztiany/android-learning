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

public class AvatarView extends View {

    private static final float WIDTH = UnitConverter.dpToPx(300);
    private static final float PADDING = UnitConverter.dpToPx(40);
    private static final float BORDER_WIDTH = UnitConverter.dpToPx(10);

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Bitmap avatar;
    private final Xfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private final RectF cut = new RectF();
    private final RectF border = new RectF();

    public AvatarView(Context context) {
        super(context);
    }

    {
        avatar = getAvatar((int) WIDTH);
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

        canvas.drawOval(border, paint);
        //这里为什么要 saveLayer，因为我们的这个View 是在一个根View上的，根View已经在 canvas 上画了一层，如果不使用 saveLayer 的话，
        //那么 setXfermode 将没有效果，因为此时 SRC_IN 中，将使用整个 canvas 作为蒙版。
        int saved = canvas.saveLayer(cut, paint);

        canvas.drawOval(cut, paint);
        paint.setXfermode(xfermode);
        canvas.drawBitmap(avatar, PADDING, PADDING, paint);
        paint.setXfermode(null);

        canvas.restoreToCount(saved);
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