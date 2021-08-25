package com.example.textdraw;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1.获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启一个fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //3.向FrameLayout容器添加MainFragment,现在并未真正执行
        transaction.add(R.id.frameLayout, MainFragment.newInstance(), MainFragment.class.getName());
        // 4.提交事务，真正去执行添加动作
        transaction.commit();
    }

}
