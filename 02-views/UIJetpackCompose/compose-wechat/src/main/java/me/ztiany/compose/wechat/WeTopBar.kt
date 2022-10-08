package me.ztiany.compose.wechat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.ztiany.compose.wechat.ui.theme.WeTheme

@Composable
fun WeTopBar(title: String, onBack: (() -> Unit)? = null) {
    Box(
        Modifier
            .background(WeTheme.colors.background)
            .fillMaxWidth()
    ) {
        Row(
            Modifier
                .height(48.dp)
        ) {
            if (onBack != null) {
                Icon(
                    painterResource(R.drawable.ic_back),
                    null,
                    Modifier
                        .clickable(onClick = onBack)
                        .align(Alignment.CenterVertically)
                        .size(36.dp)
                        .padding(8.dp),
                    tint = WeTheme.colors.icon
                )
            }
            Spacer(Modifier.weight(1f))
            val viewModel: WeViewModel = viewModel()
            Icon(
                painterResource(R.drawable.ic_palette),
                "切换主题",
                Modifier
                    .clickable {
                        viewModel.theme = when (viewModel.theme) {
                            WeTheme.Theme.Light -> WeTheme.Theme.Dark
                            WeTheme.Theme.Dark -> WeTheme.Theme.NewYear
                            WeTheme.Theme.NewYear -> WeTheme.Theme.Light
                        }
                    }
                    .align(Alignment.CenterVertically)
                    .size(36.dp)
                    .padding(8.dp),
                tint = WeTheme.colors.icon
            )
        }
        Text(title, Modifier.align(Alignment.Center), color = WeTheme.colors.textPrimary)
    }
}