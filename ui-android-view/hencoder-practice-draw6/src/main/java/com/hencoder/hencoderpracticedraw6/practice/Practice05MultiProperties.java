package com.hencoder.hencoderpracticedraw6.practice;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hencoder.hencoderpracticedraw6.R;

public class Practice05MultiProperties extends ConstraintLayout {
    Button animateBt;
    ImageView imageView;
    private boolean mIsHidden = true;

    public Practice05MultiProperties(Context context) {
        super(context);
    }

    public Practice05MultiProperties(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice05MultiProperties(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        animateBt = (Button) findViewById(R.id.animateBt);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setScaleX(0);
        imageView.setScaleY(0);
        imageView.setAlpha(0f);
        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsHidden) {
                    PropertyValuesHolder translation = PropertyValuesHolder.ofFloat(View.TRANSLATION_X.getName(), 0, 600);
                    PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat(View.ROTATION.getName(), 0, 360 * 4);
                    PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA.getName(), 0, 1);
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X.getName(), 0, 1);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y.getName(), 0, 1);
                    ObjectAnimator.ofPropertyValuesHolder(imageView, translation, rotation, alpha, scaleX, scaleY).setDuration(1000).start();
                } else {
                    PropertyValuesHolder translation = PropertyValuesHolder.ofFloat(View.TRANSLATION_X.getName(), 600, 0);
                    PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat(View.ROTATION.getName(), 360 * 4, 0);
                    PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA.getName(), 1, 0);
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X.getName(), 1, 0);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y.getName(), 1, 0);
                    ObjectAnimator.ofPropertyValuesHolder(imageView, translation, rotation, alpha, scaleX, scaleY).setDuration(1000).start();
                }
                mIsHidden = !mIsHidden;
            }
        });
    }
}
