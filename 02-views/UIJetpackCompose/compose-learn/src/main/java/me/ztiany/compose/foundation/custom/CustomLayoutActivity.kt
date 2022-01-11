package me.ztiany.compose.foundation.custom

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.commom.UIJetpackComposeTheme

private fun MenuItem.onClick(onClick: () -> Unit) {
    setOnMenuItemClickListener {
        onClick()
        true
    }
}

class CustomLayoutActivity : AppCompatActivity() {

    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        "Example01" to { Example01() },
        "Example02" to { Example02() },
        "TwoTexts" to { TwoTexts() },
    )

    private val layout = mutableStateOf("Example01")

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
            menu.add(it.key).onClick { layout.value = it.key }
        }
        return super.onCreateOptionsMenu(menu)
    }

}