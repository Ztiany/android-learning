package me.ztiany.compose.facility.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WanAndroidApi {

    @GET("banner/json")
    suspend fun loadBanners(): ApiResult<List<Banner>>

    @GET("article/top/json")
    suspend fun loadTopArticles(): ApiResult<List<Article>>

    @GET("article/list/{page}/json")
    suspend fun loadHomeArticles(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
    ): ApiResult<WanAndroidPager<Article>>

    @GET("user_article/list/{pageNo}/json")
    suspend fun loadSquareArticles(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
    ): ApiResult<WanAndroidPager<Article>>

}