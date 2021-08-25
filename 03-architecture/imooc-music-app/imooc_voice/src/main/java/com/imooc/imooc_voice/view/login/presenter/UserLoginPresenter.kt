package com.imooc.imooc_voice.view.login.presenter

import com.google.gson.Gson
import com.imooc.imooc_voice.api.KRequestCenter
import com.imooc.imooc_voice.api.MockData
import com.imooc.imooc_voice.model.login.LoginEvent
import com.imooc.imooc_voice.model.user.User
import com.imooc.imooc_voice.utils.UserManager
import com.imooc.imooc_voice.view.login.inter.IUserLoginPresenter
import com.imooc.imooc_voice.view.login.inter.IUserLoginView
import com.imooc.lib_network.okhttp.listener.DisposeDataListener
import com.imooc.lib_network.okhttp.utils.ResponseEntityToModule
import org.greenrobot.eventbus.EventBus

class UserLoginPresenter constructor(userLoginView: IUserLoginView) : IUserLoginPresenter, DisposeDataListener {

    private val mUserLoginView: IUserLoginView = userLoginView

    override fun login(name: String, password: String) {
        mUserLoginView.showLoadingView()
        KRequestCenter.login(this)
    }

    override fun onSuccess(responseObj: Any) {
        mUserLoginView.hideLoadingView()
        //保存到单例对象中
        UserManager.mUser = (responseObj as User)
        //发送登陆Event
        EventBus.getDefault().post(LoginEvent())
        mUserLoginView.finishActivity()
    }

    override fun onFailure(reasonObj: Any) {
        mUserLoginView.hideLoadingView()
        //onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.LOGIN_DATA, User::class.java))
        onSuccess(Gson().fromJson(MockData.LOGIN_DATA, User::class.java))
        mUserLoginView.showLoginFailedView()
    }
}