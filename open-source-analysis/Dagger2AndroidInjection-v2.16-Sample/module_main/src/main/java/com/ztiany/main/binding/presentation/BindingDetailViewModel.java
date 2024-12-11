package com.ztiany.main.binding.presentation;

import android.databinding.ObservableField;

import com.ztiany.main.binding.data.BindingBean;
import com.ztiany.main.binding.data.BindingDataSource;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 18:35
 */
public class BindingDetailViewModel {

    @Inject
    BindingDataSource mBindingDataSource;

    public final ObservableField<String> name = new ObservableField<>();

    @Inject
    BindingDetailViewModel() {

    }

    void start(String id) {
        mBindingDataSource.loadBindingBean(id, new BindingDataSource.Callback() {
            @Override
            public void onLoadSuccess(BindingBean bindingBean) {
                super.onLoadSuccess(bindingBean);
                name.set(bindingBean.getName());
            }
        });

    }


}
