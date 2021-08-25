package com.ztiany.view.animation.reversal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ztiany.view.R;

import androidx.appcompat.app.AppCompatActivity;

public class ReversalActivity extends AppCompatActivity {

    private View first_View;
    private View second_View;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_activity_reversal);

        first_View = findViewById(R.id.first);
        second_View = findViewById(R.id.second);
        bt = (Button) findViewById(R.id.bt);
    }

    public void startFirstAnimation(View v) {
        //first_View动画：1.翻转动画；2.透明度动画；3.缩放动画
        //1.翻转
        ObjectAnimator firstRotationAnim = ObjectAnimator.ofFloat(first_View, "rotationX", 0f, 25f);
        firstRotationAnim.setDuration(300);
        //firstRotationAnim.start();

        //2.透明度
        ObjectAnimator firstAlphaAnim = ObjectAnimator.ofFloat(first_View, "alpha", 1f, 0.5f);
        firstAlphaAnim.setDuration(200);

        //3.缩放动画
        ObjectAnimator firstScaleXAnim = ObjectAnimator.ofFloat(first_View, "scaleX", 1f, 0.8f);
        firstScaleXAnim.setDuration(300);
        ObjectAnimator firstScaleYAnim = ObjectAnimator.ofFloat(first_View, "scaleY", 1f, 0.8f);
        firstScaleYAnim.setDuration(300);
        //改正向旋转设置监听，执行完毕后再执行反向旋转
        //firstRotationAnim.addUpdateListener(listener)
        ObjectAnimator firstResumeRotationAnim = ObjectAnimator.ofFloat(first_View, "rotationX", 25f, 0f);
        firstResumeRotationAnim.setDuration(200);
        firstResumeRotationAnim.setStartDelay(200);//延迟执行
        //由于缩放造成了离顶部有一段距离，需要平移上去
        ObjectAnimator firstTranslationAnim = ObjectAnimator.ofFloat(first_View, "translationY", 0f, -0.1f * first_View.getHeight());
        firstTranslationAnim.setDuration(200);

        //第二个View执行平移动画--往上平移
        ObjectAnimator secondTranslationAnim = ObjectAnimator.ofFloat(second_View, "translationY", second_View.getHeight(), 0f);
        secondTranslationAnim.setDuration(200);
        secondTranslationAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                second_View.setVisibility(View.VISIBLE);
                bt.setClickable(false);
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(firstRotationAnim, firstAlphaAnim, firstScaleXAnim, firstScaleYAnim, firstResumeRotationAnim, firstTranslationAnim, secondTranslationAnim);
        set.start();
    }

    public void startSecondAnimation(View v) {
        //first_View动画：1.翻转动画；2.透明度动画；3.缩放动画

        //1.翻转
        ObjectAnimator firstRotationAnim = ObjectAnimator.ofFloat(first_View, "rotationX", 0f, 25f);
        firstRotationAnim.setDuration(300);
        //firstRotationAnim.start();

        //2.透明度
        ObjectAnimator firstAlphaAnim = ObjectAnimator.ofFloat(first_View, "alpha", 0.5f, 1f);
        firstAlphaAnim.setDuration(200);

        //3.缩放动画
        ObjectAnimator firstScaleXAnim = ObjectAnimator.ofFloat(first_View, "scaleX", 0.8f, 1f);
        firstScaleXAnim.setDuration(300);
        ObjectAnimator firstScaleYAnim = ObjectAnimator.ofFloat(first_View, "scaleY", 0.8f, 1f);
        firstScaleYAnim.setDuration(300);

        //改正向旋转设置监听，执行完毕后再执行反向旋转
        //firstRotationAnim.addUpdateListener(listener)
        ObjectAnimator firstResumeRotationAnim = ObjectAnimator.ofFloat(first_View, "rotationX", 25f, 0f);
        firstResumeRotationAnim.setDuration(200);
        firstResumeRotationAnim.setStartDelay(200);//延迟执行
        //由于缩放造成了离顶部有一段距离，需要平移上去
        ObjectAnimator firstTranslationAnim = ObjectAnimator.ofFloat(first_View, "translationY", -0.1f * first_View.getHeight(), 0f);
        firstTranslationAnim.setDuration(200);

        //第二个View执行平移动画--往上平移
        ObjectAnimator secondTranslationAnim = ObjectAnimator.ofFloat(second_View, "translationY", 0f, second_View.getHeight());
        secondTranslationAnim.setDuration(300);
        secondTranslationAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                second_View.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bt.setClickable(true);
            }

        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(firstRotationAnim, firstAlphaAnim, firstScaleXAnim, firstScaleYAnim, firstResumeRotationAnim, firstTranslationAnim, secondTranslationAnim);
        set.start();
    }

}