package com.ztiany.view.animation.square;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.ztiany.view.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-09-28 21:05
 */
public class SquareAnimationFragment extends Fragment {

    private TextView one, tow, three, four;

    private boolean isZoomed = false;
    private int mMeasuredWidth;
    private int mMeasuredHeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.animation_suqare, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    private void setupViews(View view) {
        one = view.findViewById(R.id.one);
        tow = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);

        WindowManager windowManager = requireActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels - 1;
        setViewWidth(one, widthPixels);
        setViewWidth(tow, widthPixels);
        setViewWidth(three, widthPixels);
        setViewWidth(four, widthPixels);

        view.post(new Runnable() {
            @Override
            public void run() {
                mMeasuredWidth = one.getMeasuredWidth();
                mMeasuredHeight = one.getMeasuredHeight();
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isZoomed) {
                    startZoomOne();
                } else {
                    startScaleOne();
                }
                isZoomed = !isZoomed;
            }
        });

    }

    private void startScaleOne() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1F, 0F);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams oneLayoutParams = one.getLayoutParams();
                ViewGroup.LayoutParams towLayoutParams = tow.getLayoutParams();

                oneLayoutParams.width = (int) (mMeasuredWidth + mMeasuredWidth * animatedValue);
                oneLayoutParams.height = (int) (mMeasuredHeight + mMeasuredHeight * animatedValue);

                towLayoutParams.height = oneLayoutParams.height;

                one.setLayoutParams(oneLayoutParams);
                tow.setLayoutParams(towLayoutParams);
            }
        });

        valueAnimator.setDuration(1500);
        valueAnimator.start();
    }

    private void setViewWidth(TextView view, int widthPixels) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) (widthPixels * 1.0F / 2 + 0.5F);
    }

    private void startZoomOne() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1F);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams oneLayoutParams = one.getLayoutParams();
                ViewGroup.LayoutParams towLayoutParams = tow.getLayoutParams();

                oneLayoutParams.width = (int) (mMeasuredWidth + mMeasuredWidth * animatedValue);
                oneLayoutParams.height = (int) (mMeasuredHeight + mMeasuredHeight * animatedValue);

                towLayoutParams.height = oneLayoutParams.height;

                one.setLayoutParams(oneLayoutParams);
                tow.setLayoutParams(towLayoutParams);
            }
        });

        valueAnimator.setDuration(1500);
        valueAnimator.start();
    }

}