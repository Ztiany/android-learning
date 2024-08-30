package me.ztiany.compose.facility.widget.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

data class PageData<T>(
    val data: T? = null,
    val isLoading: Boolean = false,
    val refreshError: Throwable? = null,
)

sealed class DataSate {
    data class Success<T>(val data: T) : DataSate()
    data class Error(val error: Throwable) : DataSate()
    data object Loading : DataSate()
    data object Empty : DataSate()
}

private fun <T> PageData<T>.toState(): DataSate {
    if (data != null) {
        return DataSate.Success(data)
    }
    if (isLoading) {
        return DataSate.Loading
    }
    if (refreshError != null) {
        return DataSate.Error(refreshError)
    }
    return DataSate.Empty
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> StateBox(
    modifier: Modifier = Modifier,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    pageState: State<PageData<T>>,
    onRefresh: (DataSate) -> Unit,
    stateConfigs: StateConfigs = StateBoxDefaults.stateConfigs,
    loadingContent: LoadingComponent = StateBoxDefaults.loadingComponent,
    emptyContent: EmptyComponent = StateBoxDefaults.emptyComponent,
    errorContent: ErrorComponent = StateBoxDefaults.errorComponent,
    networkErrorContent: ErrorComponent = StateBoxDefaults.networkErrorComponent,
    serverErrorContent: ErrorComponent = StateBoxDefaults.serverErrorComponent,
    content: @Composable (BoxScope.(T) -> Unit),
) {

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefresh(pageState.value.toState())
        }
    }

    LaunchedEffect(pageState.value.isLoading) {
        if (!pageState.value.isLoading) {
            pullToRefreshState.endRefresh()
        }
    }

    Box(modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        when (val state = pageState.value.toState()) {
            DataSate.Empty -> {
                emptyContent(stateConfigs.empty) {
                    onRefresh(state)
                }
            }

            is DataSate.Error -> {
                // TODO: differentiate between network and server errors
                errorContent(state.error, stateConfigs.error) {
                    onRefresh(state)
                }
            }

            DataSate.Loading -> {
                loadingContent()
            }

            is DataSate.Success<*> -> {
                content(this, state.data as T)
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}