package me.ztiany.compose.facility.refreshstate

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable

object RefreshStateBoxConfig {

    internal var loadingComponent: @Composable (BoxScope.() -> Unit)? = null
    internal var emptyComponent: @Composable (BoxScope.(DataSate.Empty) -> Unit)? = null
    internal var errorComponent: @Composable (BoxScope.(DataSate.Error) -> Unit)? = null

    fun loadingComponent(component: @Composable (BoxScope.() -> Unit)? = null) {
        loadingComponent = component
    }

    fun errorComponent(component: @Composable (BoxScope.(DataSate.Error) -> Unit)? = null) {
        errorComponent = component
    }

    fun emptyComponent(component: @Composable (BoxScope.(DataSate.Empty) -> Unit)? = null) {
        emptyComponent = component
    }

}
