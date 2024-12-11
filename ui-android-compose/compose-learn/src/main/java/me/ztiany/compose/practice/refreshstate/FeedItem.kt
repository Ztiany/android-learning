package me.ztiany.compose.practice.refreshstate

import me.ztiany.compose.facility.data.Banner

sealed interface FeedItem

data class BannerVO(
    val id: String = "main_banner_vo",
    val list: List<Banner>,
) : FeedItem

data class ArticleVO(
    val id: Int,
    val isTop: Boolean = false,
    val isCollected: Boolean = false,
    val author: String,
    val title: String,
    val url: String,
    val category: String,
    val updateTime: String,
) : FeedItem