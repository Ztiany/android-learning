package me.ztiany.compose.practice.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.base.foundation.state.Idle
import com.android.base.foundation.state.Loading

@Composable
fun LoadingScreen(
    viewModel: LoadingViewModel = hiltViewModel(),
) {
    val remarkState = viewModel.remarkState.collectAsStateWithLifecycle()
    val updateState = viewModel.updateRemarkState.collectAsStateWithLifecycle(Idle)

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Query)
    }

    LoadingDialog { remarkState.value }
}

@Composable
private fun LoadingDialog(state: () -> com.android.base.foundation.state.State<*, *, *>) {
    if (state() !is Loading) {
        return
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false)
    ) {
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .widthIn(max = screenWidth * 0.65F)
                .wrapContentHeight()
                .padding((16.dp)),
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = "Please wait...",
                    modifier = Modifier
                        // wrapContentSize() 修饰符会使 Text 组件的大小适应其内容，即组件的尺寸仅根据其内容来调整。
                        // 而 Alignment.Center 参数会将 Text 组件在父容器中居中对齐。
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }
}