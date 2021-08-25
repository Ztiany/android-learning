package com.ztiany.main.binding.presentation;

import com.ztiany.main.binding.data.BindingBean;
import com.ztiany.main.binding.data.BindingDataSource;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 17:39
 */
public class BindingListPresenter implements Contract.Presenter {

    private Contract.View mBindingListView;

    @Inject
    BindingDataSource mBindingDataSource;

    @Inject
    BindingListPresenter() {
    }

    @Override
    public void setView(Contract.View bindingListView) {
        mBindingListView = bindingListView;
    }

    @Override
    public void start() {
        mBindingDataSource.loadBindingBeanList(new BindingDataSource.Callback() {
            @Override
            public void onLoadSuccess(List<BindingBean> bindingBeanList) {
                mBindingListView.showList(bindingBeanList);
            }
        });
    }
}
