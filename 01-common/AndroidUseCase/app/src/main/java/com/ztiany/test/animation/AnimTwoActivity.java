package com.ztiany.test.animation;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import com.ztiany.test.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AnimTwoActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 允许使用transitions
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        String transition = getIntent().getStringExtra("transition");

        switch (transition) {
            case "explode":
                // 设置进入时进入动画
                Explode explode = new Explode();
                explode.setDuration(1000);
                getWindow().setEnterTransition(explode);
                break;

            case "slide":
                Slide slide = new Slide();
                slide.setDuration(1000);
                getWindow().setEnterTransition(slide);
                break;

            case "fade":
                Fade fade = new Fade();
                fade.setDuration(1000);
                getWindow().setEnterTransition(fade);
                break;

            case "share":
                break;
        }
        // 所有操作在设置内容视图之前
        setContentView(R.layout.activity_anim_two);
    }
}
