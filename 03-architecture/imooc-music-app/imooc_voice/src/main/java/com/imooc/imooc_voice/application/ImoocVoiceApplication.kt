package com.imooc.imooc_voice.application

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.imooc.lib_audio.app.AudioHelper
import com.imooc.lib_share.share.ShareManager
import com.imooc.lib_update.app.UpdateHelper
import com.imooc.lib_video.app.VideoHelper

class ImoocVoiceApplication : Application() {

    //伴身对象实现单例
    companion object {
        lateinit var mInstance: ImoocVoiceApplication
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        init()
    }

    private fun init() {
        //视频SDK初始化
        VideoHelper.init(mInstance)
        //音频SDK初始化
        AudioHelper.init(mInstance)
        //分享SDK初始化
        ShareManager.initSDK(mInstance)
        //更新组件下载
        UpdateHelper.init(mInstance)
        //ARouter初始化
        ARouter.init(mInstance)
    }
}