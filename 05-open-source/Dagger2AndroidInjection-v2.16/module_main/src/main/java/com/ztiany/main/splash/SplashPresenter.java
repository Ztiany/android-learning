package com.ztiany.main.splash;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-17 18:39
 */
class SplashPresenter {

    private SplashView mSplashView;

    @Inject
    SplashPresenter(SplashView splashView) {
        mSplashView = splashView;
    }

    void start() {
        mSplashView.showMessage("SplashPresenter started");
    }

}
