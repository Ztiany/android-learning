package com.ztiany.kotlin.anko.common

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.*

/**
 * 实现AnkoLogger就可以使用Log功能
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2017-07-09 23:47
 */
class LoggerActivity : AppCompatActivity(), AnkoLogger {

    private val log = AnkoLogger<LoggerActivity>()

    private val logWithASpecificTag = AnkoLogger("my_tag")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info("London is the capital of Great Britain")
        debug(5) // .toString() method will be executed
        warn(null) // "null" will be printed
        log.debug("abc")
        log.info("abc")
        log.error("abc")
        logWithASpecificTag.error("abc")
    }
}