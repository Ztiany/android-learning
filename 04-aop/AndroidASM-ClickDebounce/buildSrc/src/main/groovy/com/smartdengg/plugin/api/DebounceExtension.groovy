package com.smartdengg.plugin.api

import groovy.transform.ToString

/**
 * 创建时间:  2019/06/03 12:13 <br>
 * 作者:  SmartDengg <br>
 * 描述: 插件配置
 * */
@ToString
class DebounceExtension {

    static final String NAME = "debounce"

    /*是否打印日志*/
    boolean loggable

    /*定义需要排除的类：Class->[method-name]*/
    Map<String, List<String>> exclusion = [:]

}
