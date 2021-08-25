package com.imooc.imooc_voice.utils

import com.imooc.imooc_voice.model.user.User

/*
 * 用户管理类，单例对象
 */
object UserManager {

    var mUser: User? = null

    //计算属性
    var hasLogined = false
        get() = this.mUser != null
}