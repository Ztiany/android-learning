package me.ztiany.compose.facility.widget.state

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.ztiany.compose.R

data class StateConfig(
    private val description: String = "",
    @StringRes private val descriptionRes: Int = 0,
    private val action: String = "",
    @StringRes private val actionRes: Int = 0,
    @DrawableRes val iconRes: Int = 0,
) {

    @Composable
    @ReadOnlyComposable
    fun getDescription(): String {
        return if (descriptionRes != 0) {
            stringResource(id = descriptionRes)
        } else {
            description
        }
    }

    @Composable
    @ReadOnlyComposable
    fun getAction(): String {
        return if (actionRes != 0) {
            stringResource(id = actionRes)
        } else {
            action
        }
    }

}

data class StateConfigs(
    val empty: StateConfig,
    val error: StateConfig,
    val netError: StateConfig,
    val serverError: StateConfig,
)

typealias OnRetry = () -> Unit
typealias LoadingComponent = @Composable BoxScope.() -> Unit
typealias EmptyComponent = @Composable BoxScope.(config: StateConfig, onRetry: OnRetry) -> Unit
typealias ErrorComponent = @Composable BoxScope.(error: Throwable, config: StateConfig, onRetry: OnRetry) -> Unit

object StateBoxDefaults {

    private var _stateConfigs: StateConfigs = StateConfigs(
        empty = StateConfig(descriptionRes = R.string.page_error_no_data, actionRes = R.string.page_error_retry),
        error = StateConfig(descriptionRes = R.string.page_error_load_failed, actionRes = R.string.page_error_retry, iconRes = R.drawable.img_error),
        netError = StateConfig(
            descriptionRes = R.string.page_error_no_network,
            actionRes = R.string.page_error_retry,
            iconRes = R.drawable.img_no_network
        ),
        serverError = StateConfig(
            descriptionRes = R.string.page_error_server_error,
            actionRes = R.string.page_error_retry,
            iconRes = R.drawable.img_error
        ),
    )

    internal var loadingComponent: LoadingComponent = { DefaultLoadingComponent() }

    internal var emptyComponent: EmptyComponent = { config, onRetry ->
        DefaultEmptyComponent(config, onRetry)
    }

    internal var errorComponent: ErrorComponent = { error, config, onRetry ->
        DefaultErrorComponent(error, config, onRetry)
    }

    internal var networkErrorComponent: ErrorComponent = { error, config, onRetry ->
        DefaultErrorComponent(error, config, onRetry)
    }

    internal var serverErrorComponent: ErrorComponent = { error, config, onRetry ->
        DefaultErrorComponent(error, config, onRetry)
    }

    val stateConfigs: StateConfigs
        get() = _stateConfigs

    fun stateConfigs(configs: StateConfigs) {
        _stateConfigs = configs
    }

    fun loadingComponent(component: @Composable BoxScope.() -> Unit) {
        loadingComponent = component
    }

    fun emptyComponent(component: @Composable BoxScope.(StateConfig, OnRetry) -> Unit) {
        emptyComponent = component
    }

    fun errorComponent(component: @Composable BoxScope.(Throwable, StateConfig, OnRetry) -> Unit) {
        errorComponent = component
    }

    fun networkErrorComponent(component: @Composable BoxScope.(Throwable, StateConfig, OnRetry) -> Unit) {
        networkErrorComponent = component
    }

    fun serverErrorComponent(component: @Composable BoxScope.(Throwable, StateConfig, OnRetry) -> Unit) {
        serverErrorComponent = component
    }

}

@Composable
private fun BoxScope.DefaultLoadingComponent() {
    CircularProgressIndicator(
        modifier = Modifier
            .width(48.dp)
            .align(Alignment.Center),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
private fun BoxScope.DefaultEmptyComponent(config: StateConfig, onRetry: OnRetry) {
    Column(modifier = Modifier.align(Alignment.Center)) {
        if (config.iconRes != 0) {
            Image(
                painter = painterResource(id = config.iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        val description = config.getDescription()
        if (description.isNotEmpty()) {
            Text(
                text = description,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(if (config.iconRes != 0) 10.dp else 0.dp)
            )
        }
        val action = config.getAction()
        if (action.isNotEmpty()) {
            Button(
                onClick = { onRetry() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(if (description.isNotEmpty() || config.iconRes != 0) 10.dp else 0.dp),
            ) {
                Text(text = action)
            }
        }
    }
}

@Composable
private fun BoxScope.DefaultErrorComponent(error: Throwable, config: StateConfig, onRetry: OnRetry) {
    Column(modifier = Modifier.align(Alignment.Center)) {
        if (config.iconRes != 0) {
            Image(
                painter = painterResource(id = config.iconRes),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        val description = config.getDescription()
        if (description.isNotEmpty()) {
            Text(
                text = description,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(if (config.iconRes != 0) 10.dp else 0.dp)
            )
        }
        val action = config.getAction()
        if (action.isNotEmpty()) {
            Button(
                onClick = { onRetry() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(if (description.isNotEmpty() || config.iconRes != 0) 10.dp else 0.dp),
            ) {
                Text(text = action)
            }
        }
    }
}