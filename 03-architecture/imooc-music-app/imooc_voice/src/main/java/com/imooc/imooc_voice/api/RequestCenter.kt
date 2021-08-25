package com.imooc.imooc_voice.api

import com.imooc.imooc_voice.model.discory.BaseRecommandModel
import com.imooc.imooc_voice.model.discory.BaseRecommandMoreModel
import com.imooc.imooc_voice.model.friend.BaseFriendModel
import com.imooc.imooc_voice.model.user.User
import com.imooc.lib_network.okhttp.CommonOkHttpClient
import com.imooc.lib_network.okhttp.listener.DisposeDataHandle
import com.imooc.lib_network.okhttp.listener.DisposeDataListener
import com.imooc.lib_network.okhttp.request.CommonRequest
import com.imooc.lib_network.okhttp.request.RequestParams

private const val ROOT_URL = "http://imooc.com/api"
//val ROOT_URL = "http://39.97.122.129"

/**
 * 首页请求接口
 */
private const val HOME_RECOMMAND = "$ROOT_URL/module_voice/home_recommand"

private const val HOME_RECOMMAND_MORE = "$ROOT_URL/module_voice/home_recommand_more"

private const val HOME_FRIEND = "$ROOT_URL/module_voice/home_friend"

/**
 * 登陆接口
 */
private const val LOGIN = "$ROOT_URL/module_voice/login_phone"

//可以以顶级函数定义，就可以去掉最外层的类
class KRequestCenter {

    companion object {

        //根据参数发送所有post请求
        private fun getRequest(url: String, params: RequestParams?, listener: DisposeDataListener,
                               clazz: Class<*>) {
            CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), DisposeDataHandle(listener, clazz))
        }

        fun requestRecommandData(listener: DisposeDataListener) {
            getRequest(HOME_RECOMMAND, null, listener, BaseRecommandModel::class.java)
        }

        fun requestRecommandMore(listener: DisposeDataListener) {
            getRequest(HOME_RECOMMAND_MORE, null, listener, BaseRecommandMoreModel::class.java)
        }

        fun requestFriendData(listener: DisposeDataListener) {
            getRequest(HOME_FRIEND, null, listener, BaseFriendModel::class.java)
        }

        fun login(listener: DisposeDataListener) {
            val params = RequestParams()
            params.put("mb", "18734924592")
            params.put("pwd", "999999q")
            getRequest(LOGIN, params, listener, User::class.java)
        }
    }

}