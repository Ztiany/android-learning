package me.ztiany.compose.facility.data

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.extension.map
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ActivityRetainedScoped
class SampleRepository @Inject constructor(
    private val homeApiContext: ServiceContext<WanAndroidApi>,
) {

    suspend fun loadBanner(): List<Banner> {
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadBanners()
            }
        }
    }

    suspend fun loadTopArticles(): List<Article> {
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadTopArticles()
            }
        }
    }

    suspend fun loadHomeArticles(page: Int, pageSize: Int): List<Article> {
        Timber.d("loadHomeArticles: page = $page, pageSize = $pageSize")
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadHomeArticles(page, pageSize)
            }
        }.datas
    }

    suspend fun loadHomeArticlesCallback(page: Int, pageSize: Int): CallResult<List<Article>> {
        Timber.d("loadHomeArticles: page = $page, pageSize = $pageSize")
        return withContext(Dispatchers.IO) {
            homeApiContext.apiCall {
                loadHomeArticles(page, pageSize)
            }
        }.map { it.datas }
    }

}