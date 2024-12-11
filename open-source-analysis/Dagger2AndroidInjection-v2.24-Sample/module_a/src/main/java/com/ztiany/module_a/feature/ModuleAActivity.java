package com.ztiany.module_a.feature;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ztiany.base.presentation.ErrorHandler;
import com.ztiany.module_a.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:20
 */
public class ModuleAActivity extends AppCompatActivity implements ModuleAView {

    @Inject
    ModuleAPresenter mModuleA_Presenter;
    @Inject
    ErrorHandler mErrorHandler;

    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_a_activity);
        mTextView = (TextView) findViewById(R.id.module_a_tv_message);
        mModuleA_Presenter.start();
    }

    @Override
    public void showMessage(String message) {
        mTextView.setText(message);
    }

    public void throwError(View view) {
        mErrorHandler.handleError(new IllegalArgumentException("ModuleA mock Exception"));
    }
}
