package com.imooc.lib_base.ft_login.service;

import android.content.Context;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.imooc.lib_base.ft_login.model.user.User;

/**
 * Login模块对外提供的所有功能
 */
public interface LoginService extends IProvider {

  User getUserInfo();

  void removeUser();

  boolean hasLogin();

  void login(Context context);
}
