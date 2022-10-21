package me.ztiany.compose.wechat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import me.ztiany.compose.wechat.WeViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Home(viewModel: WeViewModel) {
    Column(Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState()
        //顶部
        HorizontalPager(count = 4, Modifier.weight(1F), pagerState) { page ->
            when (page) {
                0 -> ChatList(viewModel.chats)
                1 -> ContactList()
                2 -> DiscoveryList()
                3 -> MeList()
            }
        }
        // 创建 CoroutineScope
        val scope = rememberCoroutineScope()
        //底部
        Column {
            WeBottomBar(pagerState.currentPage) { page ->
                // 点击页签后，在协程里翻页
                scope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        }
    }
}