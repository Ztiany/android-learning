package me.ztiany.compose.rengxuxian

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import kotlin.math.max
import kotlin.math.roundToInt

/*
    难道等级：绘制 < 布局 < 触摸反馈（多点触摸）
 */
@Composable
fun Lesson09() {
    Column(Modifier.verticalScroll(rememberScrollState(0))) {
        //自定义绘制
        APIDemo()
        CanvasAPI()

        //自定义布局
        ModifierLayoutAPI()//自定义布局方式1
        LayoutAPI({
            Text("自定义布局")
            Text("自定义布局")
            Text("自定义布局")
            Text("自定义布局")
            Text("自定义布局")
        }, Modifier.background(Color.Red))//自定义布局方式2
    }
}

@Composable
private fun LayoutAPI(content: @Composable () -> Unit, modifier: Modifier) {
    //Layout 具有完全的开放性【这里模拟一个简单的 Column】
    Layout(
        //content 里面用于声明内容
        content = content,
        modifier,
        //measurePolicy 用于定义布局算法
        { measurables, constraints ->

            var width = 0
            var height = 0

            //测量
            val placeables = mutableListOf<Placeable>()
            measurables.forEach {
                val element = it.measure(constraints)
                width = max(width, element.width)
                height += element.height
                placeables.add(element)
            }

            //布局
            layout(width, height) {
                var currentHeight = 0
                placeables.forEach {
                    it.place(0, currentHeight)
                    currentHeight += it.height
                }
            }
        })
}

@Composable
private fun ModifierLayoutAPI() {
    //测量的和布局的逻辑是：测量里层，然后根据测量结果向外曾报告自己的宽高。
    //layout API 只能定制自己的布局逻辑。
    Text(text = "自定义布局", Modifier
        .background(Color.Red)
        .layout {
            //measurable 要么是一个 LayoutNode，要么是一个 LayoutModifier【一层套一层】
                measurable,
                //尺寸限制
                constraints ->

            //测量逻辑：加上一个默认的 padding
            val padding = 8.dp
                .toPx()
                .roundToInt()

            val paddedConstraints = constraints
                .copy()
                .apply {
                    constrainWidth(maxWidth - padding * 2)
                    constrainHeight(maxHeight - padding * 2)
                }

            val placeable = measurable.measure(paddedConstraints)

            //摆放：layout 的返回值就是测量结果【用于向上汇报】
            layout(placeable.width + 2 * padding, placeable.height + 2 * padding) {
                placeable.placeRelative(padding, padding)
            }
        }
        .background(Color.Blue))
}

@Composable
private fun CanvasAPI() {
    //完全自定义绘制
    Canvas(Modifier.size(40.dp)) {
        drawRect(Color.Red)
    }
}

@Composable
private fun APIDemo() {
    Column {
        //drawWithContent
        Text(text = "自定义绘制", Modifier.drawWithContent {
            //自定义的绘制
            drawRect(Color.Green)
            //绘制原有的内容
            drawContent()
        })
        //drawBehind 里面的内容是在 drawContent 之后调用的
        Text(text = "自定义绘制", Modifier.drawBehind {
            //自定义的绘制
            drawRect(Color.Yellow)
        })
        //drawWithCache 中的 cache 是用于保存绘制需要的对象的。【为什么不用 remember，因为这是在 Modifier 里面，没有 Compose 的上下文】
        Text(text = "自定义绘制", Modifier.drawWithCache {
            val path = Path()
            onDrawBehind {
                path.lineTo(size.width, size.height)
                drawPath(path = path, Color.Red, style = Fill)
            }
        })

    }
}

@Composable
fun Lesson09_Intrinsic() {
    //Intrinsic 特性
    /*
        placeable 只能调用一次 measure，否则就会报错。
        此时可以依赖 Compose 的 Intrinsic【内在特性】，具体描述为：
        如果我限制你的宽，那么你会是多高，如果我限制你的高，那么你会是多宽。
        如果我限制你的宽，那么你最多/最少会是多高，如果我限制你的高，那么你最多/最少会是多宽【这个就是固有尺寸】。

    IntrinsicSize.Min 的行为：先对所有子节点进行一次模拟测量，然后用最小的那个 size 来作为自己的固有 size，最后再用这个 size 对所有子节点进行真正的测量。
     */
    Row(Modifier.height(IntrinsicSize.Min)) {
        Text("Text1")
        Divider(
            Modifier
                .width(1.dp)
                .fillMaxHeight(), Color.Red
        )
        Text("Text2")
    }
}

@Composable
fun Lesson09_Touch() {
    Modifier.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                val event = awaitPointerEvent()
            }
        }
    }
}