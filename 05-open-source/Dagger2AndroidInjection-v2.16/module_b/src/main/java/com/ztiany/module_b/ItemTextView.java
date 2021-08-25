package com.ztiany.module_b;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.ztiany.base.di.ViewInjection;
import com.ztiany.base.presentation.MessageUtils;

import javax.inject.Inject;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 16:12
 */
public class ItemTextView extends AppCompatTextView {

    @Inject
    MessageUtils mMessageUtils;

    public ItemTextView(Context context) {
        super(context);
        init();
    }

    public ItemTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ViewInjection.inject(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMessageUtils.showMessage("I am item TextView, My text is: " + getText());
            }
        });
    }

    @Subcomponent()
    public interface CustomTextViewComponent extends AndroidInjector<ItemTextView> {
        @Subcomponent.Builder
        abstract class Builder extends AndroidInjector.Builder<ItemTextView> {
        }
    }

}
