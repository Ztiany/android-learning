package me.ztiany.compose.learn.state.statetest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.ztiany.compose.facility.utils.asImmutable
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HelloViewModel @Inject constructor() : ViewModel() {

    private val _name = MutableLiveData("")
    val name = _name.asImmutable()

    fun update(name: String) {
        _name.value = name
    }

}

@Composable
fun HelloStateScreen(
    helloViewModel: HelloViewModel = hiltViewModel()
) {
    val name by helloViewModel.name.observeAsState("")
    HelloContent({ name }) {
        helloViewModel.update(it)
    }
    Timber.d("HelloStateScreen is recomposed.")
}

@Composable
private fun HelloContent(name: () -> String, onNameChanged: (String) -> Unit) {
    Timber.d("HelloContent is recomposed.")
    Column(Modifier.padding(16.dp)) {
        TextField(value = name(), onNameChanged)
    }
}