package com.ztiany.main.binding.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 15:07
 */
public class BindingRepository implements BindingDataSource {

    private volatile List<BindingBean> mBindingBeans;

    @Inject
    BindingRepository() {

    }

    @Override
    public void loadBindingBeanList(final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    callback.onLoadSuccess(createList());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void loadBindingBean(String id, Callback callback) {
        for (BindingBean bindingBean : mBindingBeans) {
            if (bindingBean.getId().equals(id)) {
                callback.onLoadSuccess(bindingBean);
                return;
            }
        }
    }

    private List<BindingBean> createList() {
        mBindingBeans = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BindingBean bindingBean = new BindingBean();
            bindingBean.setId(String.valueOf(i));
            bindingBean.setName("Bean-->" + i);
            mBindingBeans.add(bindingBean);
        }
        return mBindingBeans;
    }
}
