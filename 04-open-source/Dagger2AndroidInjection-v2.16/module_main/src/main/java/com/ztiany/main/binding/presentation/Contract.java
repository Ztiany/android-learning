package com.ztiany.main.binding.presentation;

import com.ztiany.main.binding.data.BindingBean;

import java.util.List;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 18:07
 */
public interface Contract {

    interface View{

        void showList(List<BindingBean> bindingBeanList);

    }

    interface Presenter{

        void start();

        void setView(View view);
    }

}
