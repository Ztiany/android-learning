package me.ztiany.compose.practice.paging3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import me.ztiany.compose.facility.data.SampleRepository
import me.ztiany.compose.practice.refreshstate.ArticleMapper
import javax.inject.Inject

@HiltViewModel
class Paging3ViewModel @Inject constructor(
    repository: SampleRepository,
    articleMapper: ArticleMapper,
) : ViewModel() {

    val squareFlow = repository.loadSquareArticles(
        0,
        20
    )
        .map { it.map(articleMapper::mapArticle) }
        .cachedIn(viewModelScope)
}