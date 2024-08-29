package me.ztiany.compose.facility.refreshstate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp

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
fun <T> RefreshStateBox(
    modifier: Modifier = Modifier,
    positionalThreshold: Dp = PullToRefreshDefaults.PositionalThreshold,
    enabled: () -> Boolean = { true },
    pageState: State<PageData<T>>,
    onRefresh: (DataSate) -> Unit,
    loadingComponentBlock: @Composable (BoxScope.() -> Unit)? = RefreshStateBoxConfig.loadingComponent,
    emptyComponentBlock: @Composable (BoxScope.(DataSate.Empty) -> Unit)? = RefreshStateBoxConfig.emptyComponent,
    errorComponentBlock: @Composable (BoxScope.(DataSate.Error) -> Unit)? = RefreshStateBoxConfig.errorComponent,
    contentComponentBlock: @Composable (BoxScope.(T) -> Unit),
) {
    val pullToRefreshState = rememberPullToRefreshState(positionalThreshold, enabled)

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefresh(pageState.value.toState())
        }
    }

    Box(modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = pageState.value.toState()) {
                DataSate.Empty -> {
                    emptyComponentBlock?.invoke(this, DataSate.Empty)
                }

                is DataSate.Error -> {
                    errorComponentBlock?.invoke(this, state)
                }

                DataSate.Loading -> {
                    loadingComponentBlock?.invoke(this)
                }

                is DataSate.Success<*> -> {
                    contentComponentBlock(this, state.data as T)
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}