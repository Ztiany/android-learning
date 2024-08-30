package me.ztiany.compose.facility.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.android.sdk.net.ServiceContext
import com.android.sdk.net.coroutines.CallResult
import com.android.sdk.net.extension.map
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class SampleRepository @Inject constructor(
    private val homeApiContext: ServiceContext<WanAndroidApi>,
) {

    suspend fun loadBanner(): List<Banner> {
        delay(3000)
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadBanners()
            }
        }
    }

    suspend fun loadTopArticles(): List<Article> {
        delay(3000)
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadTopArticles()
            }
        }
    }

    suspend fun loadHomeArticles(page: Int, pageSize: Int): List<Article> {
        Timber.d("loadHomeArticles: page = $page, pageSize = $pageSize")
        delay(3000)
        return withContext(Dispatchers.IO) {
            homeApiContext.executeApiCall {
                loadHomeArticles(page, pageSize)
            }
        }.datas
    }

    suspend fun loadHomeArticlesCallback(page: Int, pageSize: Int): CallResult<List<Article>> {
        Timber.d("loadHomeArticles: page = $page, pageSize = $pageSize")
        delay(3000)
        return withContext(Dispatchers.IO) {
            homeApiContext.apiCall {
                loadHomeArticles(page, pageSize)
            }
        }.map { it.datas }
    }

    fun loadSquareArticles(pageStart: Int, pageSize: Int) = Pager(
        PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            enablePlaceholders = false
        )
    ) {
        IntKeyPagingSource(
            pageStart = pageStart,
            serviceContext = homeApiContext
        ) { serviceContext, page, size ->
            serviceContext.executeApiCall {
                delay(3000)
                if (page >= 3) {
                    throw IOException("Test exception")
                }
                loadSquareArticles(page, size)
            }.datas
        }
    }.flow

}