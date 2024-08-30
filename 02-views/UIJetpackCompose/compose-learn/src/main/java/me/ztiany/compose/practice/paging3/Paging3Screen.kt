package me.ztiany.compose.practice.paging3

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import me.ztiany.compose.facility.widget.list.StateLazyColumn
import me.ztiany.compose.practice.refreshstate.ArticleItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Paging3Screen(
    viewModel: Paging3ViewModel = hiltViewModel(),
) {
    val pagingData = viewModel.squareFlow.collectAsLazyPagingItems()

    StateLazyColumn(
        modifier = Modifier.fillMaxSize(),
        lazyPagingItems = pagingData
    ) {
        items(pagingData.itemCount){
            val item = pagingData[it]
            item?.let { ArticleItem(article = item) }
        }
    }
}