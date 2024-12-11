package me.ztiany.compose.facility.widget.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import me.ztiany.compose.facility.widget.state.StateConfig
import me.ztiany.compose.facility.widget.state.StateConfigs
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateLazyColumn(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<*>,
    onRefresh: (() -> Unit) = { lazyPagingItems.refresh() },
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    lazyListState: LazyListState = rememberLazyListState(),
    stateConfigs: StateConfigs = StateBoxDefaults.stateConfigs,
    loadingContent: LoadingComponent = StateBoxDefaults.loadingComponent,
    emptyContent: EmptyComponent = StateBoxDefaults.emptyComponent,
    errorContent: ErrorComponent = StateBoxDefaults.errorComponent,
    networkErrorContent: ErrorComponent = StateBoxDefaults.networkErrorComponent,
    serverErrorContent: ErrorComponent = StateBoxDefaults.serverErrorComponent,
    loadingItem: LoadingItem = StateLazyDefaults.loadingItem,
    errorItem: ErrorItem = StateLazyDefaults.errorItem,
    noMoreItem: NoMoreItem = StateLazyDefaults.noMoreItem,
    listContent: LazyListScope.() -> Unit,
) {

    Box(modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        when {
            lazyPagingItems.loadState.refresh is LoadState.Error -> {
                errorContent((lazyPagingItems.loadState.refresh as LoadState.Error).error, stateConfigs.error) { onRefresh() }
            }

            lazyPagingItems.loadState.refresh is LoadState.Loading && !pullToRefreshState.isRefreshing -> loadingContent()

            else -> {
                LazyColumn(state = lazyListState) {
                    Timber.d("StateLazyColumn: is Recomposing")
                    listContent()
                    LoadingItem(lazyPagingItems, loadingItem, errorItem, noMoreItem)
                }

                EmptyBox(lazyPagingItems, stateConfigs.empty, onRefresh, emptyContent)
            }
        }

        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )
    }

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
}

@Suppress("FunctionName")
fun LazyListScope.LoadingItem(
    lazyPagingItems: LazyPagingItems<*>,
    loadingItem: LoadingItem = StateLazyDefaults.loadingItem,
    errorItem: ErrorItem = StateLazyDefaults.errorItem,
    noMoreItem: NoMoreItem = StateLazyDefaults.noMoreItem,
) {
    if (lazyPagingItems.itemSnapshotList.isNotEmpty()) {
        item {
            lazyPagingItems.apply {
                Timber.d("StateLazyColumn: appendState: ${loadState.append}")
                when (val appendState = loadState.append) {
                    is LoadState.Loading -> loadingItem()
                    is LoadState.Error -> errorItem(appendState.error) { retry() }
                    is LoadState.NotLoading -> {
                        if (loadState.append.endOfPaginationReached) {
                            noMoreItem()
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun BoxScope.EmptyBox(
    lazyPagingItems: LazyPagingItems<*>,
    config: StateConfig,
    onRefresh: () -> Unit,
    emptyContent: EmptyComponent,
) {
    Box(modifier = Modifier.align(Alignment.Center)) {
        if (lazyPagingItems.itemSnapshotList.isEmpty()) {
            emptyContent(config, onRefresh)
        }
    }
}