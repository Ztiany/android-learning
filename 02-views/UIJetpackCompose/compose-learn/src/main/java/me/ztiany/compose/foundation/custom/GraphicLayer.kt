package me.ztiany.compose.foundation.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * 参考：[GraphicsLayerModifier](https://github.com/Debdutta-Panda/GraphicsLayerModifier)
 */
@Composable
fun GraphicLayerScreen() {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        var alpha by remember { mutableStateOf(1f) }
        var translationX by remember { mutableStateOf(0f) }
        var translationY by remember { mutableStateOf(0f) }
        var elevation by remember { mutableStateOf(0f) }
        var scaleX by remember { mutableStateOf(1f) }
        var scaleY by remember { mutableStateOf(1f) }
        var rotationX by remember { mutableStateOf(0f) }
        var rotationY by remember { mutableStateOf(0f) }
        var rotationZ by remember { mutableStateOf(0f) }
        var cameraDistance by remember { mutableStateOf(0f) }
        var originX by remember { mutableStateOf(0.5f) }
        var originY by remember { mutableStateOf(0.5f) }
        var blurEffect by remember { mutableStateOf(0f) }
        val padding = 0.dp

        Box(
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding(48.dp)
                        .width(220.dp)
                        .height(60.dp)
                        .graphicsLayer(
                            alpha = alpha,
                            translationX = translationX * 200,
                            translationY = translationY * 200,
                            shadowElevation = elevation * 50,
                            scaleX = scaleX,
                            scaleY = scaleY,
                            rotationX = rotationX * 360,
                            rotationY = rotationY * 360,
                            rotationZ = rotationZ * 360,
                            cameraDistance = cameraDistance * 50 + 8f,
                            transformOrigin = TransformOrigin(originX, originY),
                            renderEffect = BlurEffect(blurEffect * 50 + 1, blurEffect * 50 + 1),
                            //shape = CircleShape,
                            //clip = true
                        )
                ) {
                    Text(
                        "Hello World"
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("Alpha($alpha)")
                            Slider(value = alpha, onValueChange = {
                                alpha = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("TranslationX(${translationX * 200})")
                            Slider(value = translationX, onValueChange = {
                                translationX = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("TranslationY(${translationY * 200})")
                            Slider(value = translationY, onValueChange = {
                                translationY = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("ShadowElevation(${elevation * 50})")
                            Slider(value = elevation, onValueChange = {
                                elevation = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("ScaleX($scaleX)")
                            Slider(value = scaleX, onValueChange = {
                                scaleX = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("ScaleY($scaleY)")
                            Slider(value = scaleY, onValueChange = {
                                scaleY = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("RotationX(${rotationX * 360})")
                            Slider(value = rotationX, onValueChange = {
                                rotationX = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("RotationY(${rotationY * 360})")
                            Slider(value = rotationY, onValueChange = {
                                rotationY = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("RotationZ(${rotationZ * 360})")
                            Slider(value = rotationZ, onValueChange = {
                                rotationZ = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("Camera Distance(${cameraDistance * 50 + 8})")
                            Slider(value = cameraDistance, onValueChange = {
                                cameraDistance = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("Blur($blurEffect*50+1)")
                            Slider(value = blurEffect, onValueChange = {
                                blurEffect = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("OriginX($originX)")
                            Slider(value = originX, onValueChange = {
                                originX = it
                            })
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier.padding(top = padding)
                        ) {
                            Text("OriginY($originY)")
                            Slider(value = originY, onValueChange = {
                                originY = it
                            })
                        }
                    }
                }
            }
        }
    }
}