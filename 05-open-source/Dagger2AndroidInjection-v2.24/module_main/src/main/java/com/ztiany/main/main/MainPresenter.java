package com.ztiany.main.main;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-17 17:45
 */
class MainPresenter {

    private MainView mMainView;

    @Inject
    MainPresenter(MainView mainView) {
        mMainView = mainView;
    }

    void start() {
        mMainView.showMessage("MainPresenter started");
    }

}
