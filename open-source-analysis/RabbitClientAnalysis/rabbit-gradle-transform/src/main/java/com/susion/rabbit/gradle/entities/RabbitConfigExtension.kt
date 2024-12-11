package com.susion.rabbit.gradle.entities

/**
 * susionwang at 2019-11-28
 *
 * plugin dsl config
 */
open class RabbitConfigExtension {

    /** 测速扫描范围 */
    var pageSpeedMonitorPkgs: List<String> = ArrayList()

    /** 函数耗时扫描范围 */
    var methodMonitorPkgs: List<String> = ArrayList()

    var blockCodePkgs: List<String> = ArrayList()

    /** 是否启动整个插件 */
    var enable: Boolean = true

    var printLog: Boolean = true

    /** 应用测速 */
    var enableSpeedCheck: Boolean = true

    /** 阻塞代码调用 */
    var enableBlockCodeCheck: Boolean = false

    /** 方法耗时 */
    var enableMethodCostCheck: Boolean = false

    /** 自定义阻塞代码检测列表 */
    var customBlockCodeCheckList: Set<String> = HashSet()

}