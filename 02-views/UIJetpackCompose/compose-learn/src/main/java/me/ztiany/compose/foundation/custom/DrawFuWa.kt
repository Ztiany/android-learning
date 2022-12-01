package me.ztiany.compose.foundation.custom

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R


/** 演示  drawWithCache 的使用*/
@Preview
@Composable
fun DrawFuWa() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val transition = rememberInfiniteTransition()

        val alpha by transition.animateFloat(
            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            )
        )

        /*
        有时在 DrawScope 中绘制时，会用到一些与绘制有关的对象（如ImageBitmap、Paint、Path等），当组件发生重绘时，由于
        DrawScope 会反复执行，这其中声明的对象也会随之重新创建，实际上这类对象是没必要重新创建的。如果这类对象占用内存
        空间较大，频繁多次重绘意味着这类对象会频繁地加载重建，从而导致内存抖动等问题。

        也许有人会提出疑问，将这类对象存放到外部 Composable 作用域中，并利用 remember 缓存不可以吗？当然这个做法从语法上
        来说是可行的，但这样做违反了迪米特法则，这类对象可能会被同 Composable 内其他组件依赖使用。如果将这类对象存放到全
        局静态域会更危险，不仅会污染全局命名空间，并且当该 Composable 组件离开视图树时，还会导致内存泄漏问题。由于这类对
        象只跟这次绘制有关，所以还是放在一块比较合适。

        为解决这个问题，Compose 为我们提供了 drawWithCache 方法，就是支持缓存的绘制方法。通过 drawWithCache 声明可以看到，
        需要一个传入 CacheDrawScope 作用域的 Lambda，值得注意的是返回值是 DrawResult 类型。可以在 CacheDrawScope 接口声明
        中发现仅有 onDrawBehind 与 onDrawWithContent 这两个API 提供了 DrawResult 类型返回值，实际上这两个 API 和前面所提及的
        drawBehind 与 drawWithContent 用法是完全相同的。
         */
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .size(340.dp, 300.dp)
                .onGloballyPositioned {

                }
                /*
                这里使用 drawCache 来绘制多张图片，并不断改变这些图片的透明度。假设每张图片像素尺寸都比较大，一次性把这些图片全部
                装载到内存不仅耗时，并且也会占用大量内存空间。每当透明度发生变化时，我们不希望重新加载这些图片。在这个场景下，
                只需使用 drawWithCache 方法，将图片加载过程放到缓存区中完成就可以了。
                 */
                .drawWithCache {

                    val img1 = ImageBitmap.imageResource(context.resources, R.drawable.head_portrait1)
                    val img2 = ImageBitmap.imageResource(context.resources, R.drawable.head_portrait2)
                    val img3 = ImageBitmap.imageResource(context.resources, R.drawable.head_portrait3)
                    val img4 = ImageBitmap.imageResource(context.resources, R.drawable.head_portrait4)
                    val img5 = ImageBitmap.imageResource(context.resources, R.drawable.head_portrait5)

                    // 如果在drawWithContent 或 drawBehind方法中依赖了某个可变状态，当该状态更新时，会导致当前组件重新进行绘制阶段，故也被称作重绘。
                    onDrawBehind {
                        drawImage(
                            image = img1,
                            dstSize = IntSize(100.dp.roundToPx(), 100.dp.roundToPx()),
                            dstOffset = IntOffset.Zero,
                            alpha = alpha
                        )
                        drawImage(
                            image = img2,
                            dstSize = IntSize(100.dp.roundToPx(), 100.dp.roundToPx()),
                            dstOffset = IntOffset(120.dp.roundToPx(), 0),
                            alpha = alpha
                        )
                        drawImage(
                            image = img3,
                            dstSize = IntSize(100.dp.roundToPx(), 100.dp.roundToPx()),
                            dstOffset = IntOffset(240.dp.roundToPx(), 0),
                            alpha = alpha
                        )
                        drawImage(
                            image = img4,
                            dstSize = IntSize(100.dp.roundToPx(), 100.dp.roundToPx()),
                            dstOffset = IntOffset(60.dp.roundToPx(), 120.dp.roundToPx()),
                            alpha = alpha
                        )
                        drawImage(
                            image = img5,
                            dstSize = IntSize(100.dp.roundToPx(), 100.dp.roundToPx()),
                            dstOffset = IntOffset(180.dp.roundToPx(), 120.dp.roundToPx()),
                            alpha = alpha
                        )
                    }
                }
        )
    }
}