package me.ztiany.compose.foundation.layout

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.theme.UIJetpackComposeTheme
import me.ztiany.compose.theme.onClick

class LayoutBasicActivity : AppCompatActivity() {


    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        "ArtistCard" to {
            ArtistCard {
                Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show()
            }
        },
        "QuotesDemo" to { QuotesDemo() },
        "UserPortraitDemo" to { UserPortraitDemo() },
        "InputFieldLayoutDemo" to { InputFieldLayoutDemo() },
        "ConstraintLayoutDemo" to { ConstraintLayoutDemo() },
        "SearchBar" to { SearchBar() },
    )

    private val layout = mutableStateOf("ArtistCard")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                val key = layout.value
                layouts[key]?.invoke()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        layouts.forEach {
            menu.add(it.key).onClick {
                layout.value = it.key
                supportActionBar?.title = it.key
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

}