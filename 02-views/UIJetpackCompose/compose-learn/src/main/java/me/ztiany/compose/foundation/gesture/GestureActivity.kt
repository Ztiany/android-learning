package me.ztiany.compose.foundation.gesture

import android.os.Bundle
import android.view.Menu
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import me.ztiany.compose.commom.UIJetpackComposeTheme
import me.ztiany.compose.commom.onClick

class GestureActivity : AppCompatActivity() {

    private val layouts = linkedMapOf<String, @Composable () -> Unit>(
        //高级 API
        "Clickable" to { ClickableViews() },
        "Draggable" to { DraggableViews() },
        "Swipeable" to { SwipeableViews() },
        "Transformable" to { TransformableViews() },
        "Scrollable" to { ScrollableViews() },
        "Scrollable" to { NestedScrollViews() },
        //中层 API
        "DetectTapGestures" to { DetectTapGesturesViews() },
        "DetectDragGestures" to { DetectDragGesturesViews() },
        "TransformGesture" to { TransformGestureViews() },
        "ForEachGesture" to { ForEachGestureViews() },
        //底层 API
        "AwaitPointerEvent" to { AwaitPointerEventViews() },
        "AwaitFirstDown" to { AwaitFirstDownViews() },
        "Drag" to { DragViews() },
        "AwaitDragOrCancellation" to { AwaitDragOrCancellationViews() },
        "AwaitTouchSlopOrCancellation" to { AwaitTouchSlopOrCancellationViews() }
    )

    private val layout = mutableStateOf("Clickable")

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