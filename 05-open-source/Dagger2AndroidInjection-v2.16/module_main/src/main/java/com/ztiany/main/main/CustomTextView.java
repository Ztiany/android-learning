package com.ztiany.main.main;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.base.di.ViewInjection;
import com.ztiany.base.presentation.MessageUtils;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-06-21 14:42
 */
public class CustomTextView extends AppCompatTextView {

    @Inject
    MessageUtils mMessageUtils;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewInjection.inject(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageUtils.showMessage(" I am CustomTextView");
            }
        });
    }
}
