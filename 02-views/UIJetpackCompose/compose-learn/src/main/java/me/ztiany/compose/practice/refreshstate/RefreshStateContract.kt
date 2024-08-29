package me.ztiany.compose.practice.refreshstate

internal data class QueryCondition(
    val refreshAction: Int,
    val pageStart: Int,
    val pageSize: Int,
)