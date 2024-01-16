package me.ztiany.compose.learn.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R
import timber.log.Timber

/**
 * Column/Row/Card 等布局的使用。
 */
@Composable
fun ArtistCard() {
    Column(
        Modifier
            .clickable { Timber.d("ArtistCard is clicked.") }
            .padding(16.dp)
            .fillMaxWidth()) {

        //头部
        ArtistCardTop()
        //间隔
        Spacer(Modifier.size(16.dp))
        //详情介绍
        Card(elevation = 4.dp) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = "Ah, here we go again with the big gestures and the pukeworthy chat-up lines that are supposed to be witty and original. I'm sorry, but in 99% of cases this is only going to succeed in making you look like a Massive Wanker. An Intolerable Twat. Another Dickhead who needs pulling down a peg or two with an equally witty put-down. Why must you show off with your need to impress and your peacock feathers on show? It's off-putting!"
            )
        }
    }
}

@Composable
private fun ArtistCardTop() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painterResource(id = R.drawable.profile_picture),
            "",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "Alfred Sisley", color = MaterialTheme.colors.secondaryVariant, style = MaterialTheme.typography.subtitle2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                Text(
                    text = "3 minutes ago", style = MaterialTheme.typography.body2
                )
            }
        }
    }
}



