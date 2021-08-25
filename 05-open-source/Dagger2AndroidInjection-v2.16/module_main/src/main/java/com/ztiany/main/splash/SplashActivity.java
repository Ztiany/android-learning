package com.ztiany.main.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ztiany.main.R;
import com.ztiany.main.main.MainActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SplashActivity extends AppCompatActivity implements SplashView {

    @Inject
    SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_main_activity_splash);
        mSplashPresenter.start();
    }

    public void openMain(View view) {
        startActivity(new Intent(this, MainActivity.class));
        supportFinishAfterTransition();
    }

    @Override
    public void showMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.module_main_splash_tv);
        textView.setText(message);
    }

}
