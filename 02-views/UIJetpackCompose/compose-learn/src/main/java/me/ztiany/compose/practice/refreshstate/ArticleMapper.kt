package me.ztiany.compose.practice.refreshstate

import me.ztiany.compose.facility.data.Article
import me.ztiany.compose.facility.data.Banner
import javax.inject.Inject

class ArticleMapper @Inject constructor() {

    fun mapBanner(bannerList: List<Banner>): BannerVO {
        return BannerVO(list = bannerList)
    }

    fun mapArticles(articleList: List<Article>): List<ArticleVO> {
        return articleList.map { mapArticle(it) }
    }

    fun mapArticle(article: Article): ArticleVO {
        return ArticleVO(
            article.id,
            author = article.author,
            title = article.title,
            url = article.link,
            category = "${article.superChapterName}:${article.chapterName}",
            updateTime = article.niceDate
        )
    }

}