package me.ztiany.compose.practice.simplestate

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ztiany.compose.facility.widget.state.StateBox
import me.ztiany.compose.practice.refreshstate.ArticleList
import me.ztiany.compose.practice.refreshstate.RefreshStateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleStateScreen(
    viewModel: RefreshStateViewModel = hiltViewModel(),
) {
    val articleState = viewModel.articles.collectAsStateWithLifecycle()

    StateBox(
        modifier = Modifier.fillMaxSize(),
        pageState = articleState,
        pullToRefreshState = rememberPullToRefreshState(enabled = { false }),
        onRefresh = { viewModel.refresh() }
    ) {
        ArticleList(it)
    }
}