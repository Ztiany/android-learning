package me.ztiany.compose.foundation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar() {
    var text by remember {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFD3D3D3))
            .padding(vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {

        BasicTextField(value = text, onValueChange = {
            text = it
        }, decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                Box(modifier = Modifier
                    .padding(10.dp)
                    .weight(1F), contentAlignment = Alignment.CenterStart) {
                    if (text.isEmpty()) {
                        Text(
                            text = "输入点东西看看吧~",
                            style = TextStyle(
                                color = Color(0, 0, 0, 128)
                            )
                        )
                    }
                    innerTextField()
                }
                if (text.isNotEmpty()) {
                    IconButton(onClick = { text = "" },
                        Modifier
                            .padding(horizontal = 5.dp)
                            .size(16.dp)) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            }
        }, modifier = Modifier
            .padding(horizontal = 10.dp)
            .background(Color.White, CircleShape)
            .fillMaxWidth()
        )
    }

}