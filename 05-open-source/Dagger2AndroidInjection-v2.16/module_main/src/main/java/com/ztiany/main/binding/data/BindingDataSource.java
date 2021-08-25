package com.ztiany.main.binding.data;

import java.util.List;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 14:55
 */
public interface BindingDataSource {

    void loadBindingBeanList(Callback callback);

    void loadBindingBean(String id ,Callback callback);

    abstract class Callback {

        public void onLoadSuccess(List<BindingBean> bindingBeanList) {

        }

        public void onLoadSuccess(BindingBean bindingBean) {

        }
    }


}
