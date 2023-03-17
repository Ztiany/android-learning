package me.ztiany.compose.foundation.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import me.ztiany.compose.facilities.widget.ExampleArea

@Composable
fun WidgetsScreen() {
    val context = LocalContext.current

    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        //Text
        ExampleArea(exampleName = "Text") {
            TextExample(context)
        }
        //Button
        ExampleArea(exampleName = "Button") {
            TextFiledExample(context)
        }
        //Button
        ExampleArea(exampleName = "Button") {

        }
    }
}
