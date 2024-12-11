package me.ztiany.compose.learn.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facility.widget.ExampleArea

@Composable
fun WidgetsScreen() {
    val context = LocalContext.current

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        // Text
        ExampleArea(exampleName = "Text") {
            TextExample(context)
        }

        // TextFiled
        ExampleArea(exampleName = "TextFiled") {
            TextFiledExample()
        }

        // Icon
        ExampleArea(exampleName = "Icon") {
            IconExample(context)
        }

        // Button
        ExampleArea(exampleName = "Button") {
            ButtonExample()
        }

        // Slider
        ExampleArea(exampleName = "Slider") {
            SliderExample(context)
        }

        // Image
        ExampleArea(exampleName = "Image") {
            ImageExample(context)
        }

        // Shape
        ExampleArea(exampleName = "Shape and Polygon") {
            PolygonalExample(context)
        }
    }
}
