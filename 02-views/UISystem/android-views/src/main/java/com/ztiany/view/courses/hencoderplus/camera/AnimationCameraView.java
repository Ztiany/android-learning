package com.ztiany.view.courses.hencoderplus.camera;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.ztiany.view.courses.hencoderplus.Utils;
import com.ztiany.view.utils.UnitConverter;

public class AnimationCameraView extends View {

    private static final int IMAGE_WIDTH = UnitConverter.dpToPx(200);
    private static final int IMAGE_PADDING = UnitConverter.dpToPx(20);

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap image;
    Camera camera = new Camera();

    private float flipRotation = 0;
    private float bottomFlip = 0;
    private float topFlip = 0;

    public AnimationCameraView(Context context) {
        super(context);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doAnimation();
            }
        });
    }

    public void setBottomFlip(float bottomFlip) {
        this.bottomFlip = bottomFlip;
        invalidate();
    }

    public void setFlipRotation(float flipRotation) {
        this.flipRotation = flipRotation;
        invalidate();
    }

    public void setTopFlip(float topFlip) {
        this.topFlip = topFlip;
        invalidate();
    }

    private void doAnimation() {
        ObjectAnimator bottomFlip = ObjectAnimator.ofFloat(this, "bottomFlip", 0F, 45F);
        ObjectAnimator flipRotation = ObjectAnimator.ofFloat(this, "flipRotation", 0F, 270F);
        ObjectAnimator topFlip = ObjectAnimator.ofFloat(this, "topFlip", 0F, -45F);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(bottomFlip, flipRotation, topFlip);
        animatorSet.setDuration(3000);
        animatorSet.start();
    }

    {
        image = Utils.getAvatar(getResources(), IMAGE_WIDTH);
        camera.setLocation(0, 0, Utils.getZForCamera()); // -8 * 72
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(IMAGE_PADDING + IMAGE_WIDTH / 2, IMAGE_PADDING + IMAGE_WIDTH / 2);
        canvas.rotate(-flipRotation);
        camera.save();
        camera.rotateX(topFlip);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-IMAGE_WIDTH, -IMAGE_WIDTH, IMAGE_WIDTH, 0);
        canvas.rotate(flipRotation);
        canvas.translate(-(IMAGE_PADDING + IMAGE_WIDTH / 2), -(IMAGE_PADDING + IMAGE_WIDTH / 2));
        canvas.drawBitmap(image, IMAGE_PADDING, IMAGE_PADDING, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(IMAGE_PADDING + IMAGE_WIDTH / 2, IMAGE_PADDING + IMAGE_WIDTH / 2);
        canvas.rotate(-flipRotation);
        //保存状态
        camera.save();
        camera.rotateX(bottomFlip);
        camera.applyToCanvas(canvas);
        //恢复状态
        camera.restore();
        canvas.clipRect(-IMAGE_WIDTH, 0, IMAGE_WIDTH, IMAGE_WIDTH);//裁切图片下面的一半
        canvas.rotate(flipRotation);
        canvas.translate(-(IMAGE_PADDING + IMAGE_WIDTH / 2), -(IMAGE_PADDING + IMAGE_WIDTH / 2));
        canvas.drawBitmap(image, IMAGE_PADDING, IMAGE_PADDING, paint);
        canvas.restore();
    }

}