package com.ztiany.main.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ztiany.main.R;
import com.ztiany.main.binding.BindingActivity;

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
        mMessageTv = (TextView) findViewById(R.id.module_main_main_tv);
        mMainPresenter.start();
    }

    @Override
    public void showMessage(CharSequence message) {
        mMessageTv.setText(message);
    }

    public void openBinding(View view) {
        startActivity(new Intent(this, BindingActivity.class));
    }
}
