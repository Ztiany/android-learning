package me.ztiany.compose.facility.state

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/*
 copy from: <https://github.com/Petterpx/StateX>.
 for more information, please visit the link <https://juejin.cn/post/7042110419439190024>.
 */

/** 状态页 data */
sealed class PageData {

    data class Success<T>(val data: T) : PageData()

    data class Error(
        val throwable: Throwable? = null,
        val value: Any? = null
    ) : PageData()

    data object Loading : PageData()

    data class Empty(val value: Any? = null) : PageData()
}

/** 页面状态 */
class PageState(state: PageData) {

    /** 内部交互的状态 */
    internal var interactionState by mutableStateOf(state)

    /** 供外部获取当前状态 */
    val state: PageData get() = interactionState

    /** 供外部修改当前状态 */
    fun changeState(pageData: PageData) {
        interactionState = pageData
    }

    val isLoading: Boolean
        get() = interactionState is PageData.Loading

    companion object {
        fun loading() = PageData.Loading

        fun <T> success(t: T) = PageData.Success(t)

        fun empty(value: Any? = null) = PageData.Empty(value)

        fun error(
            throwable: Throwable? = null,
            exceptionMessage: Any? = null
        ) = PageData.Error(throwable, exceptionMessage)
    }
}

@Composable
fun rememberPageState(state: PageData = PageData.Loading): PageState {
    return rememberSaveable {
        PageState(state)
    }
}

@Composable
fun <T> StateBox(
    modifier: Modifier = Modifier,
    pageState: PageState = rememberPageState(),
    loading: () -> Unit,
    loadingComponentBlock: @Composable (BoxScope.() -> Unit)? = StateBoxConfig.loadingComponent,
    emptyComponentBlock: @Composable (BoxScope.(PageData.Empty) -> Unit)? = StateBoxConfig.emptyComponent,
    errorComponentBlock: @Composable (BoxScope.(PageData.Error) -> Unit)? = StateBoxConfig.errorComponent,
    contentComponentBlock: @Composable (BoxScope.(PageData.Success<T>) -> Unit)
) {
    Box(modifier = modifier) {
        when (pageState.interactionState) {
            is PageData.Success<*> -> contentComponentBlock(pageState.interactionState as PageData.Success<T>)

            is PageData.Loading -> {
                loadingComponentBlock?.invoke(this)
                loading.invoke()
            }

            is PageData.Error -> StateBoxCompose({
                pageState.interactionState = PageData.Loading
            }) {
                errorComponentBlock?.invoke(this, pageState.interactionState as PageData.Error)
            }
            is PageData.Empty -> emptyComponentBlock?.invoke(
                this,
                pageState.interactionState as PageData.Empty
            )
        }
    }
}

@Composable
private fun StateBoxCompose(block: () -> Unit, content: @Composable BoxScope.() -> Unit) {
    Box(
        Modifier.clickable {
            block.invoke()
        },
        content = content
    )
}
