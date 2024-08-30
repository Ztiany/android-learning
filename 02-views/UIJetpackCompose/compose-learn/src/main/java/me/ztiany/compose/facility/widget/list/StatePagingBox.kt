package me.ztiany.compose.facility.widget.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import me.ztiany.compose.facility.widget.state.EmptyComponent
import me.ztiany.compose.facility.widget.state.ErrorComponent
import me.ztiany.compose.facility.widget.state.LoadingComponent
import me.ztiany.compose.facility.widget.state.StateBoxDefaults
import me.ztiany.compose.facility.widget.state.StateConfigs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatePagingBox(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<*>,
    onRefresh: (() -> Unit) = { lazyPagingItems.refresh() },
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    stateConfigs: StateConfigs = StateBoxDefaults.stateConfigs,
    loadingContent: LoadingComponent = StateBoxDefaults.loadingComponent,
    emptyContent: EmptyComponent = StateBoxDefaults.emptyComponent,
    errorContent: ErrorComponent = StateBoxDefaults.errorComponent,
    networkErrorContent: ErrorComponent = StateBoxDefaults.networkErrorComponent,
    serverErrorContent: ErrorComponent = StateBoxDefaults.serverErrorComponent,
    listContent: @Composable BoxScope.() -> Unit,
) {

    LaunchedEffect(pullToRefreshState.isRefreshing) {
        if (pullToRefreshState.isRefreshing) {
            onRefresh()
        }
    }

    LaunchedEffect(lazyPagingItems.loadState.refresh) {
        if (lazyPagingItems.loadState.refresh !is LoadState.Loading) {
            pullToRefreshState.endRefresh()
        }
    }

    Box(modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {

        when {
            lazyPagingItems.loadState.refresh is LoadState.Error -> {
                errorContent((lazyPagingItems.loadState.refresh as LoadState.Error).error, stateConfigs.error) { onRefresh() }
            }

            lazyPagingItems.loadState.refresh is LoadState.Loading && !pullToRefreshState.isRefreshing -> loadingContent()

            else -> {
                listContent()
                EmptyBox(lazyPagingItems, stateConfigs.empty, onRefresh, emptyContent)
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }
}
