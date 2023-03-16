package me.ztiany.compose.facilities.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Entrance(
    val name: String,
    val onNavigating: () -> Unit
)

@Composable
fun EntranceList(spanCount: Int = 2, entranceList: List<Entrance>) {
    LazyVerticalGrid(columns = GridCells.Fixed(spanCount),
        // content padding
        contentPadding = PaddingValues(
            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
        ),

        //items
        content = {
            items(entranceList.size) {
                Button(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                    onClick = {
                        entranceList[it].onNavigating()
                    }) {
                    Text(text = entranceList[it].name)
                }
            }
        })
}