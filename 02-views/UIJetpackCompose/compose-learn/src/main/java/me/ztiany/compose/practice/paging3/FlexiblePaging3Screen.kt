package me.ztiany.compose.practice.paging3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import me.ztiany.compose.facility.widget.list.LoadingItem
import me.ztiany.compose.facility.widget.list.StatePagingBox
import me.ztiany.compose.practice.refreshstate.ArticleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexiblePaging3Screen(
    viewModel: Paging3ViewModel = hiltViewModel(),
) {
    val pagingData = viewModel.squareFlow.collectAsLazyPagingItems()

    StatePagingBox(
        modifier = Modifier.fillMaxSize(),
        lazyPagingItems = pagingData
    ) {
        LazyColumn(Modifier.fillMaxSize()) {
            items(pagingData.itemCount) {
                val item = pagingData[it]
                item?.let { ArticleItem(article = item) }
            }
            LoadingItem(pagingData)
        }
    }
}