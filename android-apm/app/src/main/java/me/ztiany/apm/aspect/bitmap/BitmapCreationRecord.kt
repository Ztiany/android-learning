package me.ztiany.apm.aspect.bitmap

/**
 * A record for bitmap creation.
 */
data class BitmapCreationRecord(
    val width: Int,
    val height: Int,
    val bytesPerPixel: Int,
    val format: Int,
    val time: Long,
    val nativePtr: Long,
    val path: String?,
    val stackTrace: String?,
    val scene: String?,
)