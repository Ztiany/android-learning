package me.ztiany.compose.facilities.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed interface Item

data class Entrance(
    val name: String,
    val onNavigating: () -> Unit
) : Item

data class Header(
    val name: String
) : Item

@Composable
fun EntranceList(spanCount: Int = 1, entranceList: List<Item>) {
    LazyVerticalGrid(columns = GridCells.Fixed(spanCount),
        // content padding
        contentPadding = PaddingValues(
            start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
        ),

        //items
        content = {
            items(entranceList) {
                //TODO: make header full span.
                if (it is Header) {
                    Text(text = it.name, style = MaterialTheme.typography.h5)
                } else if (it is Entrance) {
                    Button(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        onClick = {
                            it.onNavigating()
                        }) {
                        Text(text = it.name, fontSize = 12.sp)
                    }
                }
            }
        })
}