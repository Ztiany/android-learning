package me.ztiany.compose.practice.refreshstate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.sdk.net.coroutines.onError
import com.android.sdk.net.coroutines.onSuccess
import com.android.sdk.net.extension.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import me.ztiany.compose.facility.data.SampleRepository
import me.ztiany.compose.facility.widget.state.PageData
import javax.inject.Inject

/*
 MVI 架构原则：

    1. 在 ViewModel 中使用 Flow 承载数据，这样可以保证数据的一致性。ViewModel 中不应该直接使用
        Compose 的 State，这样可以避免与 Compose 的强耦合。也就是说，如果不使用 Compose 来构建
         UI，ViewModel 也可以独立存在。这也保证了可测试性。
    2. 针对页面封装 State 类，用于表示页面的状态。State 使用 sealed class，这样可以保证状态的完备性。
    4. sealed class 中承载数据的 State 类使用 data class，其持有的数据应该是不可变的，或者是可变但是
        可被观察的（即这个变化可以触发 Composable 重新重组）。不通过修改 sealed class 中的 data class
        的数据来触发 Composable 重新绘制，这样可以避免不必要的重组（即实现在 Success 页面中进行局部重组）。
    5. 使用列表页面，可以使用 Paging 3 来加载数据，这样可以保证数据的分页加载。
    6. 某些情况下，如果你的列表页不适合使用 Paging 3，且你的列表页中的 Item 支持增删改操作，作为妥协，
        可以使用 mutableStateListOf 来承载列表数据，这可以优化页面的性能，但是需要注意线程安全问题。
 */
@HiltViewModel
class RefreshStateViewModel @Inject constructor(
    private val sampleRepository: SampleRepository,
    private val articleMapper: ArticleMapper,
) : ViewModel() {

    private val queryCondition = MutableStateFlow(
        QueryCondition(
            refreshAction = 0,
            pageStart = 0,
            pageSize = 5
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val articles = queryCondition
        .flatMapMerge(1) {
            loadEmployeeList(it)
        }.scan(PageData<List<ArticleVO>>(isLoading = true)) { accumulator, value ->
            // always dispatch the latest data.
            if (value.data == null) {
                value.copy(accumulator.data)
            } else value
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(10000), PageData(isLoading = true))

    private fun loadEmployeeList(queryCondition: QueryCondition) = flow<PageData<List<ArticleVO>>> {
        emit(PageData(isLoading = true))

        sampleRepository.loadHomeArticlesCallback(
            queryCondition.pageStart,
            queryCondition.pageSize,
        ).map {
            articleMapper.mapArticles(it)
        }.onSuccess {
            emit(PageData(data = it))
        }.onError {
            emit(PageData(refreshError = it))
        }
    }

    fun refresh() {
        queryCondition.update {
            it.copy(refreshAction = it.refreshAction + 1)
        }
    }

}