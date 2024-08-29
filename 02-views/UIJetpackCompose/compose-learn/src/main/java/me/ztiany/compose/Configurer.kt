package me.ztiany.compose

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facility.refreshstate.DataSate
import me.ztiany.compose.facility.refreshstate.RefreshStateBoxConfig

internal fun configRefreshStateBox() {
    with(RefreshStateBoxConfig) {
        loadingComponent = { LoadingContent() }
        emptyComponent = { EmptyContent(it) }
        errorComponent = { ErrorContent(it) }
    }
}

@Composable
private fun BoxScope.LoadingContent() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(64.dp)
            .align(Alignment.Center),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
private fun BoxScope.EmptyContent(empty: DataSate.Empty) {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = "暂无数据"
    )
}

@Composable
private fun BoxScope.ErrorContent(error: DataSate.Error) {
    Text(
        modifier = Modifier.align(Alignment.Center),
        text = "加载错误：${error.error.message}"
    )
}