package com.ztiany.module_b;

import android.databinding.ObservableField;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 15:42
 */
class ListViewModel {

    final ObservableField<List<Bean>> mListObservableField = new ObservableField<>();

    @Inject
    ListViewModel() {

    }

    void start() {
        List<Bean> beanList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            beanList.add(new Bean("name->" + i, i));
        }
        mListObservableField.set(beanList);
    }

}
