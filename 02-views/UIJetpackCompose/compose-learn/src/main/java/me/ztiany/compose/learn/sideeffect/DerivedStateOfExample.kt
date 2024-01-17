package me.ztiany.compose.learn.sideeffect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun DerivedStateOfExample() {
    InternalExample()
}

@Composable
private fun InternalExample(highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")) {

    fun String.containsWord(word: List<String>) = word.all { this.contains(it) }

    val todoTasks = remember { mutableStateListOf<String>() }

    // Calculate high priority tasks only when the todoTasks or highPriorityKeywords
    // change, not on every recomposition
    val highPriorityTasks by remember {
        derivedStateOf { todoTasks.filter { it.containsWord(highPriorityKeywords) } }
    }

    Box(Modifier.fillMaxSize()) {
        /*LazyColumn {
            items(highPriorityTasks) { *//* ... *//* }
                items(todoTasks) { *//* ... *//* }
            }*/
        /* Rest of the UI where users can add elements to the list */
    }
}