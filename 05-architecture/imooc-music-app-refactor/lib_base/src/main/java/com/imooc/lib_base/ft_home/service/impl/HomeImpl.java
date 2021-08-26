package com.imooc.lib_base.ft_home.service.impl;

import android.content.Context;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.imooc.lib_base.ft_home.service.HomeService;

/**
 * 对HomeService的包装，使接口使用更方便
 */
public class HomeImpl {

  private static HomeImpl mHomeImpl = null;

  @Autowired(name = "/home/home_service") protected HomeService mHomeService;

  public static HomeImpl getInstance() {
    if (mHomeImpl == null) {
      synchronized (HomeImpl.class) {
        if (mHomeImpl == null) {
          mHomeImpl = new HomeImpl();
        }
      }
    }
    return mHomeImpl;
  }

  private HomeImpl() {
    ARouter.getInstance().inject(this);
  }

  public void startHomActivity(Context context) {
    mHomeService.startHomeActivity(context);
  }
}
