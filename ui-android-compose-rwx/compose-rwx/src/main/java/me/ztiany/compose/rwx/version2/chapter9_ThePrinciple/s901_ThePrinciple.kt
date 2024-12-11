package me.ztiany.compose.rwx.version2.chapter9_ThePrinciple

import android.content.Context
import androidx.compose.ui.platform.ComposeView

/*
1. 从 ComponentActivity.setContent{ } 看起
2. 再看 AbstractComposeView.setContent
    2.1 GlobalSnapshotManager.ensureStarted() 最底层的实现，Snapshot 在这里进行注册，Snapshot.sendApplyNotifications() 用于触发重组
    2.2 创建 AndroidComposeView
    2.3 创建 Composition，最后实在 Composer 中调用 invokeComposable(this, content)。
3. 再看 Layout 函数（任何一个 Composable 组件的底层入口），createSlotTable 相关，里面创建了 LayoutNode 对象。
 */
fun s901_ThePrinciple(context: Context) {
    ComposeView(context).setContent {

    }
}