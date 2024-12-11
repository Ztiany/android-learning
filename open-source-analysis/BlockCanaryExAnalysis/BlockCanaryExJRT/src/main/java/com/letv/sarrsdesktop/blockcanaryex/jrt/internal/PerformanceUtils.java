/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letv.sarrsdesktop.blockcanaryex.jrt.internal;

import com.letv.sarrsdesktop.blockcanaryex.jrt.BlockCanaryEx;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class PerformanceUtils {

    private static final String TAG = "PerformanceUtils";

    private static int sCoreNum = 0;
    private static long sTotalMemo = 0;

    private PerformanceUtils() {
        throw new InstantiationError("Must not instantiate this class");
    }

    /**
     * Get cpu core number
     *
     * @return int cpu core number
     */
    public static int getNumCores() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                return Pattern.matches("cpu[0-9]", pathname.getName());
            }
        }

        if (sCoreNum == 0) {
            try {
                // Get directory containing CPU info
                /*
                在Android（以及其他基于Linux的系统）中，/sys/目录是sysfs文件系统的挂载点，它提供了一种查看和调整内核中各种设备和驱动状态的接口。
                sysfs是一种虚拟文件系统，它将内核设备、驱动和其他内核子系统的信息以文件系统的形式呈现给用户空间，使得用户和应用程序能够查询系统硬件信息和修改配置。

                    /sys/目录下的信息按照设备和驱动的层次结构组织，常见的子目录包括：

                        /sys/class/：这个目录下包含了按类别组织的设备，比如/sys/class/net/下包含了网络接口的信息，/sys/class/power_supply/提供了电源和电池的信息。
                        /sys/devices/：这个目录包含了系统中所有已识别的设备的详细信息，它们按照物理连接和总线类型进行组织。
                        /sys/block/：包含了块设备（如硬盘和固态硬盘）的信息。
                        /sys/bus/：提供了按照总线类型（如USB、PCI等）组织的设备信息，可以查看特定总线类型下的设备列表和状态。
                        /sys/kernel/：包含了一些内核相关的设置和信息，比如调试信息和配置参数。

                    /sys/目录下的文件大多数是可读的文本文件，部分文件是可写的，允许用户或应用程序修改设备配置或触发某些操作。例如，可以通过写入特定值到/sys/class/backlight/目录下的某个文件中来调节屏幕亮度。

                需要注意的是，对/sys/目录下文件的读写可能需要特定的权限，而且错误的修改可能会影响系统稳定性或安全性，因此在操作前应该仔细了解相关文件的作用。

                具体参考：https://unix.stackexchange.com/questions/77036/sys-documentation
                 */
                File dir = new File("/sys/devices/system/cpu/");
                // Filter to only list the devices we care about
                File[] files = dir.listFiles(new CpuFilter());
                // Return the number of cores (virtual CPU devices)
                sCoreNum = files.length;
            } catch (Exception e) {
                Log.e(TAG, "getNumCores exception", e);
                sCoreNum = 1;
            }
        }
        return sCoreNum;
    }

    public static long getFreeMemory() {
        ActivityManager am = (ActivityManager) BlockCanaryEx.getConfig().getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem / 1024;
    }

    public static long getTotalMemory() {
        if (sTotalMemo == 0) {
            String str1 = "/proc/meminfo";
            String str2;
            String[] arrayOfString;
            long initial_memory = -1;
            FileReader localFileReader = null;
            try {
                localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();

                if (str2 != null) {
                    arrayOfString = str2.split("\\s+");
                    initial_memory = Integer.valueOf(arrayOfString[1]);
                }
                localBufferedReader.close();

            } catch (IOException e) {
                Log.e(TAG, "getTotalMemory exception = ", e);
            } finally {
                if (localFileReader != null) {
                    try {
                        localFileReader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "close localFileReader exception = ", e);
                    }
                }
            }
            sTotalMemo = initial_memory;
        }
        return sTotalMemo;
    }

}