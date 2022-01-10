package me.ztiany.compose.foundation.local

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import me.ztiany.compose.R
import me.ztiany.compose.commom.UIJetpackComposeTheme

class CompositionLocalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                CompositionLocalPage()
            }
        }
    }

    @Composable
    private fun CompositionLocalPage() {
        Column {
            Text(text = "Hello", color = MaterialTheme.colors.secondary)
            CompositionLocalExample()
            FruitText()
        }
    }

    @Composable
    private fun CompositionLocalExample() {
        Column {
            Text("Uses MaterialTheme's provided alpha")
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("Medium value provided for LocalContentAlpha")
                Text("This Text also uses the medium value")
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
                    DescendantExample()
                }
            }
        }
    }

    @Composable
    private fun DescendantExample() {
        // CompositionLocalProviders also work across composable functions
        Text("This Text uses the disabled alpha now")
    }

    @Composable
    private fun FruitText() {
        // Get `resources` from the current value of LocalContext
        val resources = LocalContext.current.resources
        Text(text =  resources.getString(R.string.fruit_title, "Apple"))
    }

}