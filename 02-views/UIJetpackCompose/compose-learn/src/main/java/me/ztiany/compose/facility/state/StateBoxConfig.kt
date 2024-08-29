package me.ztiany.compose.facility.state

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

object StateBoxConfig {

    internal var loadingComponent: @Composable (BoxScope.() -> Unit)? = null
    internal var emptyComponent: @Composable (BoxScope.(PageData.Empty) -> Unit)? = null
    internal var errorComponent: @Composable (BoxScope.(PageData.Error) -> Unit)? = null

    fun loadingComponent(component: @Composable (BoxScope.() -> Unit)? = null) {
        loadingComponent = component
    }

    fun errorComponent(component: @Composable (BoxScope.(PageData.Error) -> Unit)? = null) {
        errorComponent = component
    }

    fun emptyComponent(component: @Composable (BoxScope.(PageData.Empty) -> Unit)? = null) {
        emptyComponent = component
    }

}
