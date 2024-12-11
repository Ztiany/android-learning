package me.ztiany.apm.aspect.bitmap

data class MonitorConfig(
    /** 检查 Bitmap 是否回收的间隔，单位：秒。默认为 5 秒。*/
    val checkRecycleInterval: Long = 5,
    /** 超过这个阈值后获取堆栈，单位 byte。默认为 100KB。*/
    val obtainStackTraceThreshold: Long = 100 * 1024,
    /** 超过这个阈值后，保存像素数据为图片，以便分析内容，单位 byte。默认为 100KB。*/
    val dumpToFileThreshold: Long = 100 * 1024,
    /** 本地图片缓存写入上限，单位为 byte。*/
    val diskCacheLimitBytes: Long = 1024 * 1024 * 512,
    /** 图片还原保存路径。*/
    val imageStorageDirectory: String,
) {

    init {
        require(diskCacheLimitBytes > 0) { "diskCacheLimitBytes must be greater than 0." }
        require(dumpToFileThreshold > 0) { "dumpToFileThreshold must be greater than 0." }
        require(checkRecycleInterval > 0) { "checkRecycleInterval must be greater than 0." }
        require(obtainStackTraceThreshold > 0) { "obtainStackTraceThreshold must be greater than 0." }
        require(imageStorageDirectory.isNotEmpty()) { "imageStorageDirectory must not be empty." }
    }

}