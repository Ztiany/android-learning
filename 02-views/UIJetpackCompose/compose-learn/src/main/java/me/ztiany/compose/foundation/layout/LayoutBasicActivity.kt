package me.ztiany.compose.foundation.layout

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R

class LayoutBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtistCard {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Composable
    private fun ArtistCard(
        onClick: () -> Unit
    ) {
        val padding = 16.dp

        Column(
            Modifier
                .clickable { onClick() }
                .padding(padding)
                .fillMaxWidth()) {
            //头部
            ArtistCardTop()
            //间隔
            Spacer(Modifier.size(padding))
            //大图
            Card(elevation = 4.dp) {

            }
        }
    }

    @Composable
    private fun ArtistCardTop() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painterResource(id = R.drawable.ic_launcher_background),
                "",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(text = "Alfred Sisley", fontWeight = FontWeight.Bold)
                Text("3 minutes ago")
            }
        }
    }

}