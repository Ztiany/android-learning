package me.ztiany.apm.aspect.bitmap

data class BitmapStatistics(
    /** All created bitmap count, including the remained bitmaps and the recycled bitmaps. */
    val totalCreatedCount: Int,
    val totalMemoryUsage: Long,
    val remainedCount: Int,
    val remainedMemoryUsage: Long,
    val remainedRecords: Array<BitmapCreationRecord>?,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BitmapStatistics

        if (totalCreatedCount != other.totalCreatedCount) return false
        if (totalMemoryUsage != other.totalMemoryUsage) return false
        if (remainedCount != other.remainedCount) return false
        if (remainedMemoryUsage != other.remainedMemoryUsage) return false
        if (remainedRecords != null) {
            if (other.remainedRecords == null) return false
            if (!remainedRecords.contentEquals(other.remainedRecords)) return false
        } else if (other.remainedRecords != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalCreatedCount
        result = 31 * result + totalMemoryUsage.hashCode()
        result = 31 * result + remainedCount
        result = 31 * result + remainedMemoryUsage.hashCode()
        result = 31 * result + (remainedRecords?.contentHashCode() ?: 0)
        return result
    }

}