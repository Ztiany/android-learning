package com.imooc.imooc_voice.model

const val MINE_ID = 0x01
const val DISCORY_ID = 0x02
const val FRIEND_ID = 0x03
const val VIDEO_ID = 0x04

enum class KotCHANNEL(key: String, value: Int) {

    MY("我的", MINE_ID),

    DISCORY("发现", DISCORY_ID),

    FRIEND("朋友", FRIEND_ID),

    VIDEO("视频", VIDEO_ID);

    val key: String = key
    val value: Int = value
}