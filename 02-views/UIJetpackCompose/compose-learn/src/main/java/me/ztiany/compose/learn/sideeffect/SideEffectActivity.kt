package me.ztiany.compose.learn.sideeffect

import android.media.Image
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ztiany.compose.theme.UIJetpackComposeTheme

class SideEffectActivity : AppCompatActivity() {

    private val viewModel by viewModels<SideEffectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UIJetpackComposeTheme {
                val scaffoldState = rememberScaffoldState()
                Scaffold(scaffoldState = scaffoldState) {
                    Column(Modifier.padding(it)) {
                        //LaunchedEffect 示例
                        Divider("LaunchedEffect")
                        SideEffectExample(state = viewModel.uiState, scaffoldState)

                        //rememberCoroutineScope 示例
                        Divider("rememberCoroutineScope")
                        Example02(scaffoldState)

                        //rememberUpdatedState 示例
                        Divider("rememberUpdatedState")
                        Example03(scaffoldState) {}

                        //DisposableEffect 示例
                        Divider("DisposableEffect")
                        Example04(this@SideEffectActivity, {}, {})

                        //SideEffect 示例
                        Divider("SideEffect")
                        Example05(User("Alien", "SVIP"))

                        //SideEffect 示例
                        Divider("produceState")
                        Example06("https://beauty.com/girl01.jpeg", ImageRepository.newDefault())

                        //SideEffect 示例
                        Divider("derivedStateOf")
                        Example07(listOf("Review"))
                    }
                }
            }
        }
    }

    @Composable
    private fun Divider(text: String) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        Text(
            text = text,
            Modifier
                .background(Color.Cyan)
                .padding(5.dp)
                .fillMaxWidth()
        )
    }

    @Composable
    private fun Example02(scaffoldState: ScaffoldState) {
        // Creates a CoroutineScope bound to the MoviesScreen's lifecycle
        val scope = rememberCoroutineScope()
        // Content
        Column {
            Button(
                onClick = {
                    // Create a new coroutine in the event handler to show a snackbar
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Something happened!")
                    }
                }
            ) {
                Text("Press me")
            }
        }
    }

    @Composable
    private fun Example03(scaffoldState: ScaffoldState, onTimeout: () -> Unit) {
        // This will always refer to the latest onTimeout function that
        // LandingScreen was recomposed with
        val currentOnTimeout by rememberUpdatedState(onTimeout)

        // Create an effect that matches the lifecycle of LandingScreen.
        // If LandingScreen recomposes, the delay shouldn't start again.
        LaunchedEffect(true) {//这里传一个 ture，则 LaunchedEffect 不会因为重组而重启，但是同样需要注意这样做的危害性。
            delay(3000)
            currentOnTimeout()
        }
    }

    @Composable
    private fun Example04(
        lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
        onStart: () -> Unit, // Send the 'started' analytics event
        onStop: () -> Unit // Send the 'stopped' analytics event
    ) {
        // Safely update the current lambdas when a new one is provided
        val currentOnStart by rememberUpdatedState(onStart)
        val currentOnStop by rememberUpdatedState(onStop)

        // If `lifecycleOwner` changes, dispose and reset the effect
        DisposableEffect(lifecycleOwner) {
            // Create an observer that triggers our remembered callbacks
            // for sending analytics events
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    currentOnStart()
                } else if (event == Lifecycle.Event.ON_STOP) {
                    currentOnStop()
                }
            }

            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the Composition, remove the observer
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        /* Home screen content */
    }

    @Composable
    private fun Example05(user: User) {
        val analytics = rememberAnalytics(user)
    }

    @Composable
    private fun rememberAnalytics(user: User): FirebaseAnalytics {
        val analytics: FirebaseAnalytics = remember {
            FirebaseAnalytics.newDefault()
        }

        // On every successful composition, update FirebaseAnalytics with
        // the userType from the current User, ensuring that future analytics
        // events have this metadata attached
        SideEffect {
            analytics.setUserProperty("userType", user.userType)
        }

        return analytics
    }

    @Composable
    private fun Example06(url: String, imageRepository: ImageRepository) {
        val image = loadNetworkImage(url = url, imageRepository = imageRepository)
    }

    @Composable
    private fun loadNetworkImage(
        url: String,
        imageRepository: ImageRepository
    ): State<Result<Image>> {
        // Creates a State<T> with Result.Loading as initial value
        // If either `url` or `imageRepository` changes, the running producer
        // will cancel and will be re-launched with the new inputs.
        return produceState<Result<Image>>(initialValue = Result.Loading, url, imageRepository) {
            // In a coroutine, can make suspend calls
            val image = imageRepository.load(url)
            // Update State with either an Error or Success result.
            // This will trigger a recomposition where this State is read
            value = if (image == null) {
                Result.Error
            } else {
                Result.Success(image)
            }
        }
    }

    @Composable
    fun Example07(highPriorityKeywords: List<String> = listOf("Review", "Unblock", "Compose")) {

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
}