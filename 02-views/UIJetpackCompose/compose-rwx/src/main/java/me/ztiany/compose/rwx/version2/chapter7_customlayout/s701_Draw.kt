package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import me.ztiany.compose.rwx.R
import kotlin.math.roundToInt

@Composable
fun S701_Draw() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DrawBehindDemo()
        Spacer(modifier = Modifier.size(10.dp))
        CanvasDemo()
        Spacer(modifier = Modifier.size(10.dp))
        GraphicsLayerDemo()
    }
}

@Composable
private fun GraphicsLayerDemo() {
    val image = ImageBitmap.imageResource(R.drawable.avatar_rengwuxian)
    Canvas(modifier = Modifier
        .size(100.dp)
        // 底层是 View 的三维旋转 或者是 RenderNode 的旋转，它们不支持多维旋转(XYZ 只能选一个)。
        .graphicsLayer {
            rotationX = 45F
            //rotationY = 45F
        }) {
        drawRect(Color.Red, style = Stroke(1.dp.toPx()))
        drawImage(image, dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()))
    }

    // 如果要用多维旋转，还是要回到原生的 Canvas。
    val paint by remember {
        mutableStateOf(Paint())
    }

    val camera by remember {
        mutableStateOf(android.graphics.Camera())
    }

    val rotationAnimatable = remember {
        Animatable(0F)
    }

    LaunchedEffect(Unit){
        rotationAnimatable.animateTo(
            targetValue = 360F,
            animationSpec = androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(2000),
                repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
            )
        )
    }

    Canvas(modifier = Modifier.size(100.dp)) {
        drawIntoCanvas {
            // 利用 Camera 叠加 Camera 的旋转实现多维旋转。
            it.translate(size.width / 2, size.height / 2)
            it.rotate(-45F)

            camera.save()
            camera.rotateX(rotationAnimatable.value)
            camera.applyToCanvas(it.nativeCanvas)
            camera.restore()

            it.rotate(45F)
            it.translate(-size.width / 2, -size.height / 2)

            it.drawImageRect(
                image,
                dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()),
                paint = paint
            )
        }
    }
}

@Composable
private fun CanvasDemo() {
    Canvas(modifier = Modifier.size(100.dp)) {
        drawRect(Color.Red, style = Stroke(1.dp.toPx()))
        drawLine(Color.Red, strokeWidth = 2.dp.toPx(), start = Offset(0f, size.height / 2), end = Offset(size.width, size.height / 2))
    }

    val image = ImageBitmap.imageResource(R.drawable.avatar_rengwuxian)
    Canvas(modifier = Modifier.size(100.dp)) {
        drawRect(Color.Red, style = Stroke(1.dp.toPx()))
        // 不会裁减图片，即使超出了 Canvas 的范围
        rotate(45F) {
            drawImage(image, dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()))
        }
    }
}

@Composable
private fun DrawBehindDemo() {
    Column {
        Text(text = "Demo Text", Modifier.drawBehind {
            drawRect(Color.Red, style = Stroke(1.dp.toPx()))
        })
        Text(text = "Demo Text", Modifier.drawBehind {
            drawRect(Color.Blue)
            drawLine(Color.Red, strokeWidth = 2.dp.toPx(), start = Offset(0f, size.height / 2), end = Offset(size.width, size.height / 2))
        })
        Text(text = "Demo Text", Modifier.drawBehind {
            drawRect(Color.LightGray)
        })
        Text(text = "Demo Text", Modifier.drawBehind {
            drawRect(Color.Yellow)
        })
        Text(text = "Demo Text", Modifier.drawBehind {
            drawRect(Color.Magenta)
        })
    }
}
