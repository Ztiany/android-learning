package me.ztiany.bt.kit.arch

import android.app.Activity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Collect flow in an [Activity] or sometimes in a [DialogFragment].
 *
 * Notes: call the method in [Activity.onCreate].
 */
fun <T> LifecycleOwner.collectFlowRepeatedlyOnLifecycle(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    data: Flow<T>,
    onResult: suspend (result: T) -> Unit,
) {
    runRepeatedlyOnLifecycle(activeState) {
        data.collectLatest {
            onResult(it)
        }
    }
}

/** A extension method to call repeatOnLifecycle on lifecycleScope. */
fun LifecycleOwner.runRepeatedlyOnLifecycle(
    activeState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
) {
    lifecycleScope.launch {
        repeatOnLifecycle(activeState) {
            block(this)
        }
    }
}