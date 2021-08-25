package com.ztiany.test.animation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ztiany.test.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class AnimActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_main);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void explode(View view) {
        Intent intent = new Intent(this, AnimTwoActivity.class);
        intent.putExtra("transition", "explode");
        //将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void slide(View view) {
        Intent intent = new Intent(this, AnimTwoActivity.class);
        intent.putExtra("transition", "slide");
        //将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fade(View view) {
        Intent intent = new Intent(this, AnimTwoActivity.class);
        intent.putExtra("transition", "fade");
        //将原先的跳转改成如下方式
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void share(View view) {
        Intent intent = new Intent(this, AnimTwoActivity.class);
        //共享元素
        ImageView share = (ImageView) findViewById(R.id.iv_share);
        intent.putExtra("transition", "share");
        //将原先的跳转改成如下方式，注意这里面的第三个参数决定了ActivityTwo 布局中的android:transitionName的值，它们要保持一致
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, share, "image").toBundle());

    }

}
