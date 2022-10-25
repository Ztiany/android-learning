package me.ztiany.compose.rwx

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.ztiany.compose.rwx.ui.theme.UIJetpackComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainList(buildEntranceList()) {
                        startActivity(Intent(this, ContentActivity::class.java).apply {
                            putExtra("name", it)
                        })
                    }
                }
            }
        }
    }


}

@Composable
private fun MainList(list: List<String>, onClick: (String) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        list.forEach {
            Button(onClick = {
                onClick(it)
            },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()) {
                Text(text = it)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    UIJetpackComposeTheme {
        MainList(buildEntranceList()) {

        }
    }
}