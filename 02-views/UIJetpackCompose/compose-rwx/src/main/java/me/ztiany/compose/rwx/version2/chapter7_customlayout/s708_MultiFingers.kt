package me.ztiany.compose.rwx.version2.chapter7_customlayout

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun S708_MultiFingers() {
    Modifier.pointerInput(Unit) {
        detectTransformGestures(panZoomLock = true/* 如果设置为 true，那么只识别移动/旋转/放缩其中一个或两个手势 */) {
            // 多指触摸的中心点
                centroid,
                // pan 术语来源于影视拍摄，表示相机的平移。即拍摄电影或电视时摄影机摇摄，摇动拍摄。在图像领域，表示图像大于显示区域时，可以通过手指拖动图像来查看图像的其他部分。【pan 综合了多个手指中心点来做位移计算】
                pan,
                // 缩放比例，当前值是与上一刻相比的缩放比例
                zoom,
                // 旋转角度，当前值是与上一刻相比的旋转角度
                rotation ->
            {

            }
        }
    }
}