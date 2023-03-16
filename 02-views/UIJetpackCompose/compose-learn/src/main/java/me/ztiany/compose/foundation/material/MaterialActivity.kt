package me.ztiany.compose.foundation.material

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.theme.UIJetpackComposeTheme

private fun MenuItem.onClick(onClick: () -> Unit) {
    setOnMenuItemClickListener {
        onClick()
        true
    }
}

class MaterialActivity : AppCompatActivity() {

    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        "Content slots (Button)" to { MaterialButtonExample() },
        "Content slots (Floating)" to { ExtendedFloatingActionButtonExample() },
    )

    private val layout = mutableStateOf("Content slots (Button)")

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