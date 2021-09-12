package me.ztiany.compose.rengxuxian

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.ztiany.compose.R


@Composable
fun Lesson01_List() {
    val list = Array(200) {
        it.toString()
    }.toMutableList()

    LazyColumn(Modifier.fillMaxSize()) {
        items(list) { item ->
            Text(item)
        }
    }
}

@Composable
fun Lesson01_Basic() {
    Surface(color = MaterialTheme.colors.background) {
        Row(
            Modifier
                .padding(8.dp)
                .background(Color.Red, RoundedCornerShape(10.dp))
                .padding(10.dp)
                .clickable {

                }
        ) {
            //文字
            Text(
                "Alien",
                Modifier
                    .padding(8.dp)
                    .background(Color.Green), color = Color.Blue,
                fontSize = 20.sp
            )
            //图片
            Image(
                painterResource(id = R.drawable.ic_launcher_background),
                "",
                modifier = Modifier.clip(CircleShape)
            )

        }
    }
}