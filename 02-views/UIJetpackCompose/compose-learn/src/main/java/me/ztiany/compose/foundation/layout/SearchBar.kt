package me.ztiany.compose.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar() {
    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD3D3D3)), contentAlignment = Alignment.Center
    ) {

        BasicTextField(value = text, onValueChange = {
            text = it
        }, decorationBox = { innerTextField ->
            innerTextField()
        }, modifier = Modifier
            .padding(horizontal = 10.dp)
            .background(Color.White, CircleShape)
            .height(30.dp)
            .fillMaxWidth()
        )
    }

}