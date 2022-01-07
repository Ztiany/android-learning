package me.ztiany.compose.foundation.layout

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R
import me.ztiany.compose.commom.UIJetpackComposeTheme

class LayoutBasicActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                ArtistCard {
                    Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Composable
    private fun ArtistCard(
        onClick: () -> Unit
    ) {
        Column(
            Modifier
                .clickable { onClick() }
                .padding(16.dp)
                .fillMaxWidth()) {
            //头部
            ArtistCardTop()
            //间隔
            Spacer(Modifier.size(16.dp))
            //大图
            Card(elevation = 4.dp) {

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
                    text = "Alfred Sisley",
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = MaterialTheme.shapes.medium, elevation = 1.dp) {
                    Text(
                        text = "3 minutes ago",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
    }

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewMessageCard() {
        UIJetpackComposeTheme {
            ArtistCard {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
        }
    }

}