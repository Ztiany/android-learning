package com.imooc.ft_login.service;

import android.content.Context;
import android.util.Log;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.imooc.ft_login.LoginActivity;
import com.imooc.ft_login.manager.UserManager;
import com.imooc.lib_base.ft_login.model.user.User;
import com.imooc.lib_base.ft_login.service.LoginService;

/**
 * 登陆模块对外接口功能实现
 */
@Route(path = "/login/login_service")
public class LoginServiceImpl implements LoginService {

  @Override public User getUserInfo() {
    return UserManager.getInstance().getUser();
  }

  @Override public void removeUser() {
    UserManager.getInstance().removeUser();
  }

  @Override public boolean hasLogin() {
    return UserManager.getInstance().hasLogined();
  }

  @Override public void login(Context context) {
    LoginActivity.start(context);
  }

  @Override public void init(Context context) {
    Log.i(LoginServiceImpl.class.getSimpleName(), "init()");
  }
}
