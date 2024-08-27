package me.ztiany.compose.rwx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import me.ztiany.compose.rwx.ui.theme.UIJetpackComposeTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        Timber.w("MainActivity is created")
        setContent {
            UIJetpackComposeTheme {
                AppNavGraph(startDestination = MAIN_SCREEN)
            }
        }//content end
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppNavGraph(startDestination = MAIN_SCREEN)
}