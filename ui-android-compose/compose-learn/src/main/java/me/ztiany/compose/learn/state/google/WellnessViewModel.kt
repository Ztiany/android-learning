package me.ztiany.compose.learn.state.google

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * There are two ways to manage a state in a list item:
 *
 * 1. Change our data class WellnessTask so that checkedState becomes MutableState<Boolean> instead of Boolean, which causes Compose to track an item change. (Which we used here.)
 * 2. Copy the item you're about to mutate, remove the item from your list and re-add the mutated item to the list, which causes Compose to track that list change.
 */
data class WellnessTask(val id: Int, val label: String, val checked: MutableState<Boolean> = mutableStateOf(false))

@HiltViewModel
class WellnessViewModel @Inject constructor() : ViewModel() {

    /**
     * Don't expose the mutable list of tasks from outside the ViewModel.
     * Instead define _tasks and tasks. _tasks is internal and mutable inside the ViewModel.
     * tasks is public and read-only.
     */
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks

    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }

    fun changeTaskChecked(task: WellnessTask, checked: Boolean) {
        task.checked.value = checked
    }

}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }