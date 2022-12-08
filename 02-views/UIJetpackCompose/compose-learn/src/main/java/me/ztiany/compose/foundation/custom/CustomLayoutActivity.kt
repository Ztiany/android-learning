package me.ztiany.compose.foundation.custom

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.commom.UIJetpackComposeTheme
import me.ztiany.compose.commom.onClick


class CustomLayoutActivity : AppCompatActivity() {

    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        //layout 修饰符
        "FirstBaselineToTop" to { FirstBaselineToTopExample() },
        //Layout 组件
        "SimpleColumn" to { SimpleColumnExample() },
        //IntrinsicSize
        "TwoTexts By Row" to { TwoTextsExampleByRow() },
        "TwoTexts By Custom" to { TwoTextsExampleByCustom() },
        //SubcomposeLayout
        "TwoTexts By Subcompose" to { TwoTextsExampleBySubcomposeLayout() },
        //Canvas 组件
        "LoadingProgressBar" to { DrawLoadingProgressBar() },
        //drawBehind 修饰符
        "DrawableBehind" to { DrawRedDotBehind() },
        //drawWithContent 修饰符
        "DrawableFront" to { DrawRedDotFront() },
        //drawWithCache 修饰符
        "DrawFuWa" to { DrawFuWa() },
        //实践 + 原始 Canvas API
        "WaveLoading" to { WaveLoadingDemo() },
    )

    private val layout = mutableStateOf("FirstBaselineToTop")

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
            supportActionBar?.title = it.key
        }
        return super.onCreateOptionsMenu(menu)
    }

}