package me.ztiany.apm.aspect.memory

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import timber.log.Timber

/**
要提升应用的后台存活时长，我们可以根据设备的配置，采取不同的内存使用策略，比如根据内存调整缓存的大小，或者根据内存调整后台任务的执行频率等。

    ActivityManager 为我们提供了查询当前设备是否为低内存设备的 API：

        ActivityManager.isLowRamDevice

   当设备运行内存小于等于 1GB 时，这个 API 返回 true。有时候我们需要更灵活的判断标准，那就需要获取到设备的真实运行内存及剩余可用。我们可以通过 `ActivityManager.getMemoryInfo` 查询设备的物理内存上限及剩余。

   我们在发现某个版本的 LMK 指标劣化后，可以结合 getMemoryInfo 中的四个数据，调整下个版本的使用策略，从而减少触发 LMK 的概率。
 */
internal class MemoryClassification {

    fun install(app: Application) {
        val systemService = app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        Timber.d("isLowRamDevice: ${systemService.isLowRamDevice}")
    }

}