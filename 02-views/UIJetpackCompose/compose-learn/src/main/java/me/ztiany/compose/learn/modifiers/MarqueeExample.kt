package me.ztiany.compose.learn.modifiers

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facility.widget.ExampleArea


/**
 * see [Jetpack Compose gets official support for Marquee 🎉! Here’s how to use it!](https://medium.com/@theAndroidDeveloper/jetpack-compose-gets-official-support-for-marquee-heres-how-to-use-it-1f678aecb851)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MarqueeExample() {
    Column {
        ExampleArea(exampleName = "Text Marquee") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = "Compose has finally added support for Marquee! It's soo easy to implement! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Yes! Let's just try it now! I can't wait to see it."
                )
            }
        }
        ExampleArea(exampleName = "Everything Marquee") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(modifier = Modifier.basicMarquee()) {
                    repeat(30) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(Color.Red)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
        ExampleArea(exampleName = "Best Practice about Marquee") {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val focusRequester = remember { FocusRequester() }
                Text(
                    modifier = Modifier
                        .basicMarquee(animationMode = MarqueeAnimationMode.WhileFocused)
                        .focusRequester(focusRequester)
                        .focusable()
                        .clickable { focusRequester.requestFocus() },
                    text = "Compose has finally added support for Marquee! It's soo easy to implement! It’s not really a good idea to keep the marquee animation running indefinitely because, it might affect the battery life, and also, from a UX point of view, it just doesn't make sense. A good default would be to run the animation, only when the user indicates that he/she wants to see more of the content, by, for example, clicking on it.."
                )
            }
        }
    }

}