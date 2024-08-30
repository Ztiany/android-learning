package me.ztiany.compose.practice.refreshstate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.ztiany.compose.facility.widget.state.StateBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefreshStateScreen(
    viewModel: RefreshStateViewModel = hiltViewModel(),
) {
    val articleState = viewModel.articles.collectAsStateWithLifecycle()

    StateBox(
        modifier = Modifier.fillMaxSize(),
        pageState = articleState,
        onRefresh = { viewModel.refresh() }
    ) {
        ArticleList(it)
    }
}

@Composable
fun ArticleList(list: List<ArticleVO>) {
    LazyColumn {
        items(list, key = { it.id }) { article ->
            ArticleItem(article)
        }
    }
}

@Composable
fun ArticleItem(article: ArticleVO) {
    Card(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(5.dp)) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(1F), text = article.author)
                Text(modifier = Modifier.weight(1F), text = article.updateTime, textAlign = TextAlign.End)
            }
            Text(modifier = Modifier.fillMaxSize(), text = article.title)
            Text(modifier = Modifier.fillMaxSize(), text = article.category)
        }
    }
}
