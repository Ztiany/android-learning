package me.ztiany.compose.learn.widgets

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.ztiany.compose.R

//参考：https://jetpackcompose.cn/docs/elements/image
@Composable
fun ImageExample(context: Context) {
    Row {
        Image(
            painter = painterResource(id = R.drawable.google_logo),
            contentDescription = "GoogleLogo",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        AsyncImage(
            model = "https://picsum.photos/300/300",
            contentDescription = "Scenery",
            modifier = Modifier.size(100.dp)
        )
    }
}