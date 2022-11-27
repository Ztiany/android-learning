package me.ztiany.compose.rwx.version1

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Lesson07() {
    Column(Modifier.verticalScroll(rememberScrollState(0))) {
        Box(
            Modifier
                .padding(40.dp)
                .size(80.dp)
                .background(Color.Red)
        ) {

        }

        Box(
            Modifier
                .padding(40.dp)
                .size(width = 800.dp, height = 20.dp)
                .background(Color.Black)
        ) {

        }

        Box(
            Modifier
                .padding(40.dp)
                .size(160.dp)
                .size(80.dp)
                .background(Color.Blue)
        ) {

        }
        
        Row(Modifier.fillMaxSize()) {
            //不要这么做
            Box(
                Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .requiredSize(160.dp)
                    .background(Color.Green)
            ) {

            }
            Text(text = "|")
        }


    }
}

