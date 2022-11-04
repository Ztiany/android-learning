package com.peter.viewgrouptutorial.drawable

class StateInfo<out T>(
    val value: T,
    val state: State?,
    val add: Boolean
)

class ResourceInfo(
    val resourceId: Int,
    val state: State?,
    val add: Boolean
)