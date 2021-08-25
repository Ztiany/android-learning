package com.imooc.imooc_voice.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.imooc.imooc_voice.R
import com.imooc.imooc_voice.view.login.inter.IUserLoginView
import com.imooc.imooc_voice.view.login.presenter.UserLoginPresenter
import com.imooc.lib_commin_ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login_layout.*

class LoginActivity : BaseActivity(), IUserLoginView {

    private val mUserLoginPresenter: UserLoginPresenter = UserLoginPresenter(this)

    //伴生对象
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_layout)
        login_view.setOnClickListener {
            mUserLoginPresenter.login(getUserName(), getPassword())
        }
    }

    override fun getUserName(): String {
        return "18734924592"
    }

    override fun getPassword(): String {
        return "999999q"
    }

    override fun finishActivity() {
        this.finish()
    }

    override fun showLoginFailedView() {
    }

    override fun showLoadingView() {
    }

    override fun hideLoadingView() {
    }
}