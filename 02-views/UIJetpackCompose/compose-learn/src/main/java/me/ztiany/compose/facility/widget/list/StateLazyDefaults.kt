package me.ztiany.compose.facility.widget.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R

typealias LoadingItem = @Composable LazyItemScope.() -> Unit
typealias ErrorItem = @Composable LazyItemScope.(error: Throwable, onRetry: () -> Unit) -> Unit
typealias NoMoreItem = @Composable LazyItemScope.() -> Unit

object StateLazyDefaults {

    internal var loadingItem: LoadingItem = {
        DefaultLoadingItem()
    }

    internal var errorItem: ErrorItem = { error, onRetry ->
        DefaultErrorItem(error, onRetry)
    }

    internal var noMoreItem: NoMoreItem = {
        DefaultNoMoreItem()
    }

    fun setDefaultLoadingItem(loadingItem: LoadingItem) {
        this.loadingItem = loadingItem
    }

    fun setDefaultErrorItem(errorItem: ErrorItem) {
        this.errorItem = errorItem
    }

    fun setDefaultNoMoreItem(noMoreItem: NoMoreItem) {
        this.noMoreItem = noMoreItem
    }

}

@Composable
private fun DefaultLoadingItem() {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(20.dp),
            strokeWidth = 3.dp
        )
    }
}

@Composable
private fun DefaultNoMoreItem() {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.page_item_no_more),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DefaultErrorItem(error: Throwable, retry: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { retry() }) {
            Text(text = stringResource(id = R.string.page_item_error_retry))
        }
    }
}