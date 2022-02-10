package com.ztiany.view.animation.circular_reveal;

import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;

import com.ztiany.view.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CircularRevealActivity extends AppCompatActivity {

    private Button bt1;
    private Button bt2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animation_circular_reveal);
        bt1 = (Button) findViewById(R.id.bt1);
        bt2 = (Button) findViewById(R.id.bt2);
        setListeners();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setListeners() {
        bt1.setOnClickListener(v -> {
            //圆形水波纹揭露效果
           /* ViewAnimationUtils.createCircularReveal(
                    view, //作用在哪个View上面
                    centerX, centerY, //扩散的中心点
                    startRadius, //开始扩散初始半径
                    endRadius)//扩散结束半径*/
            Animator animator = ViewAnimationUtils.createCircularReveal(bt1, bt1.getWidth() / 2, bt1.getHeight() / 2, 0, bt1.getHeight());
            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
            //Math.hypot(x, y)
        });

        bt2.setOnClickListener(v -> {
            Animator animator = ViewAnimationUtils.createCircularReveal(bt2, 0, 0, 0, (float) Math.hypot(bt2.getWidth(), bt2.getHeight()));
            animator.setDuration(1000);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
        });
    }

}
