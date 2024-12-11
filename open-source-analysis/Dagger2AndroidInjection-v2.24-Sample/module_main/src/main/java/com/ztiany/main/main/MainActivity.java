package com.ztiany.main.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ztiany.main.R;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainView {

    @Inject
    MainPresenter mMainPresenter;

    @Inject
    Map<String, Provider<MapValue>> mMapValueMap;

    private TextView mMessageTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_main_activity_main);
        mMessageTv = findViewById(R.id.module_main_main_tv);
        mMainPresenter.start();
    }

    @Override
    public void showMessage(CharSequence message) {
        mMessageTv.setText(message);
    }

}