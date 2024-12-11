package com.susion.rabbit.monitor.instance

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.susion.rabbit.base.RabbitLog
import com.susion.rabbit.monitor.RabbitMonitor
import com.susion.rabbit.base.RabbitMonitorProtocol
import com.susion.rabbit.base.TAG_MONITOR
import com.susion.rabbit.base.entities.*
import com.susion.rabbit.storage.RabbitStorage
import com.susion.rabbit.tracer.RabbitTracerEventNotifier

/**
 * susionwang at 2019-11-14
 * 应用速度监控:
 * 1. 应用启动速度
 * 2. 页面启动速度、渲染速度
 */
internal class RabbitAppSpeedMonitor(override var isOpen: Boolean = false) : RabbitMonitorProtocol {

    private val TAG = "RabbitAppSpeedMonitor"

    private var currentPageName: String = ""
    private val pageApiStatusInfo = HashMap<String, RabbitPageApiInfo>()
    private var configInfo = RabbitAppSpeedMonitorConfig()
    private val monitorPageApiSet = HashSet<String>()
    private val apiSet = HashSet<String>()

    //避免重复统计测试时间
    private var appSpeedCanRecord = false
    private var pageSpeedCanRecord = false

    private var pageSpeedInfo: RabbitPageSpeedInfo = RabbitPageSpeedInfo()
    private var appSpeedInfo: RabbitAppStartSpeedInfo = RabbitAppStartSpeedInfo()

    //第一个对用户有效的页面 【闪屏页 or 首页】
    private var entryActivityName = ""

    override fun open(context: Context) {
        isOpen = true
        // 配置需要监控的项目
        configMonitorList(RabbitMonitor.mConfig.monitorSpeedList)
        if (entryActivityName.isNotEmpty()) {
            RabbitLog.d(TAG_MONITOR, "app speed init with page list! entryActivityName :$entryActivityName")
            monitorActivitySpeed()
        } else {
            RabbitLog.d(TAG_MONITOR, "app speed init with null")
            monitorApplicationStart()
        }

        RabbitLog.d(TAG_MONITOR, "entryActivityName : $entryActivityName")
    }

    override fun close() {
        isOpen = false
        RabbitTracerEventNotifier.appSpeedNotifier = RabbitTracerEventNotifier.FakeEventListener()
    }

    override fun getMonitorInfo() = RabbitMonitorProtocol.APP_SPEED

    /**
     * 可以从外部设置
     * */
    fun configMonitorList(speedConfig: RabbitAppSpeedMonitorConfig) {
        monitorPageApiSet.clear()
        apiSet.clear()

        speedConfig.pageConfigList.forEach {
            monitorPageApiSet.add(it.pageSimpleName)
            it.apiList.forEach { simpleUrl ->
                apiSet.add(simpleUrl)
            }
        }
        configInfo = speedConfig
        entryActivityName = speedConfig.homeActivity
    }

    /**
     * 一个请求结束
     * */
    fun markRequestFinish(requestUrl: String, costTime: Long = 0) {
        Log.d(TAG, "markRequestFinish requestUrl -> $requestUrl, costTime = $costTime")

        val curApiInfo = pageApiStatusInfo[currentPageName] ?: return
        curApiInfo.apiStatusList.forEach {
            if (requestUrl.contains(it.api)) {
                it.isFinish = true
                it.costTime = costTime
            }
        }
    }

    private fun monitorActivitySpeed() {
        RabbitTracerEventNotifier.appSpeedNotifier = object : RabbitTracerEventNotifier.TracerEvent {

            override fun applicationCreateTime(
                attachBaseContextTime: Long,
                createEndTime: Long
            ) {
                Log.d(TAG, "applicationCreateTime attachBaseContextTime -> $attachBaseContextTime, createEndTime = $createEndTime")

                appSpeedCanRecord = true
                appSpeedInfo.time = System.currentTimeMillis()
                appSpeedInfo.createStartTime = attachBaseContextTime
                appSpeedInfo.createEndTime = createEndTime
                appSpeedInfo.fullShowCostTime = 0
                RabbitLog.d(TAG_MONITOR, " --> applicationCreateTime  ${createEndTime - attachBaseContextTime}")
                if (entryActivityName.isEmpty()) {
                    appSpeedCanRecord = false
                    RabbitStorage.save(appSpeedInfo)
                }
            }

            override fun activityCreateStart(activity: Any, time: Long) {
                Log.d(TAG, "activityCreateStart activity -> $activity, time = $time")

                currentPageName = activity.javaClass.simpleName
                pageSpeedCanRecord = true
                pageSpeedInfo = RabbitPageSpeedInfo()
                pageSpeedInfo.pageName = activity.javaClass.name
                pageSpeedInfo.createStartTime = time
                resetPageApiRequestStatus(activity.javaClass.simpleName)
            }

            override fun activityCreateEnd(activity: Any, time: Long) {
                Log.d(TAG, "activityCreateEnd activity -> $activity, time = $time")
                pageSpeedInfo.createEndTime = time
            }

            override fun activityResumeEnd(activity: Any, time: Long) {
                Log.d(TAG, "activityResumeEnd activity -> $activity, time = $time")
                pageSpeedInfo.resumeEndTime = time
            }

            override fun activityDrawFinish(activityName: String, time: Long) {
                Log.d(TAG, "activityDrawFinish activitySimpleName -> ${activityName}, time = $time")
                RabbitLog.d(TAG_MONITOR, "$activityName -> activityDrawFinish")
                savePageSpeedInfoToLocal(time)
                if (entryActivityName.isNotEmpty() && appSpeedCanRecord) {
                    saveApplicationStartInfoToLocal(time, activityName)
                }
            }

            //保存页面测速信息
            private fun savePageSpeedInfoToLocal(drawFinishTime: Long) {
                Log.d(TAG, "savePageSpeedInfoToLocal pageSpeedCanRecord -> ${pageSpeedCanRecord}, drawFinishTime = $drawFinishTime")

                if (!pageSpeedCanRecord) return

                if (pageSpeedInfo.inflateFinishTime == 0L) {
                    pageSpeedInfo.time = System.currentTimeMillis()
                    pageSpeedInfo.inflateFinishTime = drawFinishTime
                    RabbitLog.d(TAG_MONITOR, "$currentPageName page inflateFinishTime ---> cost time : ${drawFinishTime - pageSpeedInfo.createStartTime}")
                }

                val apiStatus = pageApiStatusInfo[currentPageName]
                if (apiStatus != null) {
                    if (apiStatus.allApiRequestFinish()) {
                        RabbitLog.d(TAG_MONITOR, "$currentPageName page finish all request ---> cost time : ${drawFinishTime - pageSpeedInfo.createStartTime}")
                        pageSpeedCanRecord = false
                        pageSpeedInfo.fullDrawFinishTime = drawFinishTime
                        pageSpeedInfo.apiRequestCostString = Gson().toJson(apiStatus).toString()
                        RabbitStorage.save(pageSpeedInfo)
                    }
                } else {
                    pageSpeedCanRecord = false
                    pageSpeedInfo.fullDrawFinishTime = drawFinishTime
                    RabbitStorage.save(pageSpeedInfo)
                }
            }
        }

    }

    private fun resetPageApiRequestStatus(acSimpleName: String) {
        if (!monitorPageApiSet.contains(acSimpleName)) return
        var pageApiInfo = pageApiStatusInfo[acSimpleName]
        if (pageApiInfo == null) {
            pageApiInfo = RabbitPageApiInfo()
            for (apiConfigInfo in configInfo.pageConfigList) {
                if (apiConfigInfo.pageSimpleName == acSimpleName) {
                    apiConfigInfo.apiList.forEach { apiUrl ->
                        pageApiInfo.apiStatusList.add(RabbitApiInfo(apiUrl, false))
                    }
                    break
                }
            }
            pageApiStatusInfo[acSimpleName] = pageApiInfo
        } else {
            pageApiInfo.apiStatusList.forEach {
                it.isFinish = false
            }
        }
    }

    private fun monitorApplicationStart() {
        RabbitTracerEventNotifier.appSpeedNotifier =
            object : RabbitTracerEventNotifier.TracerEvent {
                override fun applicationCreateTime(
                    attachBaseContextTime: Long,
                    createEndTime: Long
                ) {
                    appSpeedInfo.time = System.currentTimeMillis()
                    appSpeedInfo.createStartTime = attachBaseContextTime
                    appSpeedInfo.createEndTime = createEndTime
                    RabbitStorage.save(appSpeedInfo)
                    RabbitLog.d(TAG_MONITOR, "monitorApplicationStart")
                }
            }
    }

    //应用启动测速信息记录
    private fun saveApplicationStartInfoToLocal(pageDrawFinishTime: Long, pageName: String) {
        Log.d(TAG, "saveApplicationStartInfoToLocal appSpeedCanRecord -> ${appSpeedCanRecord}, pageName = $pageName")

        if (!appSpeedCanRecord || pageName != entryActivityName) return

        val apiStatus = pageApiStatusInfo[entryActivityName]
        if (apiStatus != null) {
            if (apiStatus.allApiRequestFinish()) {
                appSpeedCanRecord = false
                appSpeedInfo.fullShowCostTime = pageDrawFinishTime - appSpeedInfo.createStartTime
                RabbitStorage.save(appSpeedInfo)
                RabbitLog.d(TAG_MONITOR, "saveApplicationStartInfoToLocal")
            }
        } else {
            appSpeedCanRecord = false
            appSpeedInfo.fullShowCostTime = pageDrawFinishTime - appSpeedInfo.createStartTime
            RabbitStorage.save(appSpeedInfo)
            RabbitLog.d(TAG_MONITOR, "saveApplicationStartInfoToLocal")
        }
    }

    //是否监控这个请求
    fun monitorRequest(requestUrl: String): Boolean {
        for (api in apiSet) {
            if (requestUrl.contains(api)) {
                return true
            }
        }
        return false
    }

}