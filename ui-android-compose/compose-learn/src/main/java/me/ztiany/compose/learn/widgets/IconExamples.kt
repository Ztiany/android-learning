package me.ztiany.compose.learn.widgets

import android.content.Context
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R

//参考：https://jetpackcompose.cn/docs/elements/icon
@Composable
fun IconExample(context: Context) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)

        Spacer(modifier = Modifier.size(10.dp))

        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.icon_settings_accessibility), contentDescription = "矢量图资源")

        Spacer(modifier = Modifier.size(10.dp))

        Icon(bitmap = ImageBitmap.imageResource(id = R.drawable.icon_settings_accessibility_normal), contentDescription = "图片资源")

        Spacer(modifier = Modifier.size(10.dp))

        Icon(painter = painterResource(id = R.drawable.icon_settings_accessibility_normal), contentDescription = "任意类型资源")
    }
}