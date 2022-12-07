package me.ztiany.compose.foundation.animation

import android.os.Bundle
import android.view.Menu
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.commom.UIJetpackComposeTheme
import me.ztiany.compose.commom.onClick
import me.ztiany.compose.foundation.animation.practice.FavButton
import me.ztiany.compose.foundation.animation.practice.Shimmer

class AnimationActivity : AppCompatActivity() {

    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        //======================================================
        // 高级动画 API
        //======================================================
        //AnimatedVisibility
        "AnimatedVisibility Text 1" to { AnimatedVisibilityText() },
        "AnimatedVisibility Text 2" to { AnimatedVisibilityTextStartWhenEnter() },
        "AnimatedVisibility Specified" to { AnimatedVisibilitySpecialChildren() },
        "AnimatedVisibility Customized" to { AnimatedVisibilityCustomized() },
        //AnimatedContent
        "AnimatedContent Text" to { AnimatedContentText() },
        "AnimatedContent Content" to { AnimatedContentTextContentTransform() },
        "AnimatedContent Size" to { AnimatedContentTextSizeTransform() },
        //CrossFade
        "CrossFadePage" to { CrossFadePage() },
        //Modifier.animateContentSize
        "AnimateContentSizeBox" to { AnimateContentSizeBox() },
        //======================================================
        // 低级动画 API
        //======================================================
        //AnimateXAsState
        "AnimateXAsState" to { AnimateXAsStateExample() },
        //Animatable
        "AnimatableButton" to { AnimatableExample() },
        //transition
        "Transition-SwitchBox" to { SwitchBox() },
        "Transition-Child" to { ChildTransitionSample() },
        "Transition-AV-AC" to { TransitionWithAV_AC() },
        "EncapsulateTransition" to { EncapsulateTransition() },
        "InfiniteTransition" to { InfiniteTransition() },
        //AnimationSpec
        "AnimationSpec" to { AnimationSpecExample() },
        "AnimationSpec-Spring" to { SpringDemo() },
        //AnimationVector
        "AnimationVector" to { AnimationVectorExample() },
        //Practice
        "Shimmer" to { Shimmer() },
        "FavButton" to { FavButton() },
    )

    private val layout = mutableStateOf("AnimatedVisibility Text 1")

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