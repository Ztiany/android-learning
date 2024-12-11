package me.ztiany.compose.facility.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ExampleArea(exampleName: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = exampleName,
            color = Color.White,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        content()
        Spacer(modifier = Modifier.height(20.dp))
    }
}
