package top.shixinzhang.bitmapmonitor;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

/**
 * 当前内存中的图片整体数据
 *
 * @create by : zhangshixin
 */
@Keep
public class BitmapMonitorData {

    //历史创建的总图片数
    public long createBitmapCount;

    //历史创建的总图片内存大小，单位 byte
    public long createBitmapMemorySize;

    //当前内存中还未回收的图片数
    public long remainBitmapCount;

    //当前内存中还未回收的图片内存大小，单位 byte
    public long remainBitmapMemorySize;

    //泄漏（未释放）的 bitmap 数据
    public BitmapRecord[] remainBitmapRecords;

    public BitmapMonitorData(
            long createBitmapCount, long createBitmapMemorySize,
            long remainBitmapCount, long remainBitmapMemorySize
    ) {
        this.createBitmapCount = createBitmapCount;
        this.createBitmapMemorySize = createBitmapMemorySize;
        this.remainBitmapCount = remainBitmapCount;
        this.remainBitmapMemorySize = remainBitmapMemorySize;
    }

    public String getCreateBitmapMemorySizeWithFormat() {
        return getFormatSize(createBitmapMemorySize);
    }

    public String getRemainBitmapMemorySizeWithFormat() {
        return getFormatSize(remainBitmapMemorySize);
    }

    public static String getFormatSize(long size) {
        float memory = size * 1.0f;
        String unit = "b";
        if (memory > 1024) {
            memory = memory / 1024;
            unit = "Kb";
        }
        if (memory > 1024) {
            memory = memory / 1024;
            unit = "Mb";
        }
        if (memory > 1024) {
            memory = memory / 1024;
            unit = "Gb";
        }
        return String.format(Locale.getDefault(), "%.0f %s", memory, unit);
    }


    @NonNull
    @Override
    public String toString() {
        return "BitmapMonitorData{" +
                "createBitmapCount=" + createBitmapCount +
                ", createBitmapMemorySize=" + createBitmapMemorySize +
                ", remainBitmapCount=" + remainBitmapCount +
                ", remainBitmapMemorySize=" + remainBitmapMemorySize +
                ", remainBitmapRecords=" + Arrays.toString(remainBitmapRecords) +
                '}';
    }

}