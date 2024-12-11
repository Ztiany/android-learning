/*
 * Copyright (C) 2016 MarkZhai (http://zhaiyifan.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.letv.sarrsdesktop.blockcanaryex.jrt.internal;

import com.letv.sarrsdesktop.blockcanaryex.jrt.BlockInfo;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dumps cpu usage.
 */
class CpuSampler {

    private static final String TAG = "CpuSampler";
    private static final int SAMPLE_INTERVAL = 1000;
    private static final int BUFFER_SIZE = 1000;
    private static final int BUST_TIME = (int) (SAMPLE_INTERVAL * 1.2);

    /**
     * TODO: Explain how we define cpu busy in README
     */
    private static final int MAX_ENTRY_COUNT = 10;

    private final LinkedHashMap<Long, String> mCpuInfoEntries = new LinkedHashMap<>();
    private int mPid = 0;
    private long mUserLast = 0;
    private long mSystemLast = 0;
    private long mIdleLast = 0;
    private long mIoWaitLast = 0;
    private long mTotalLast = 0;
    private long mAppCpuTimeLast = 0;

    private static final CpuSampler sInstance = new CpuSampler();

    static CpuSampler getInstance() {
        return sInstance;
    }

    void resetSampler(int pid) {
        mPid = pid;
        reset();
        doSample();
    }

    void recordSample() {
        doSample();
    }

    /**
     * Get cpu rate information
     *
     * @return string show cpu rate information
     */
    String getCpuRateInfo() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Long, String> entry : mCpuInfoEntries.entrySet()) {
            long time = entry.getKey();
            sb.append(com.letv.sarrsdesktop.blockcanaryex.jrt.internal.TimeUtils.format(time))
                    .append(' ')
                    .append(entry.getValue())
                    .append(BlockInfo.SEPARATOR);
        }
        return sb.toString();
    }

    boolean isCpuBusy(long start, long end) {
        if (end - start > SAMPLE_INTERVAL) {
            long s = start - SAMPLE_INTERVAL;
            long e = start + SAMPLE_INTERVAL;
            long last = 0;
            for (Map.Entry<Long, String> entry : mCpuInfoEntries.entrySet()) {
                long time = entry.getKey();
                if (s < time && time < e) {
                    if (last != 0 && time - last > BUST_TIME) {
                        return true;
                    }
                    last = time;
                }
            }
        }
        return false;
    }

    private void doSample() {
        BufferedReader cpuReader = null;
        BufferedReader pidReader = null;

        try {
            cpuReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), BUFFER_SIZE);
            String cpuRate = cpuReader.readLine();
            if (cpuRate == null) {
                cpuRate = "";
            }

            pidReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + mPid + "/stat")), BUFFER_SIZE);
            String pidCpuRate = pidReader.readLine();
            if (pidCpuRate == null) {
                pidCpuRate = "";
            }

            parse(cpuRate, pidCpuRate);
        } catch (Throwable throwable) {
            Log.e(TAG, "doSample: ", throwable);
        } finally {
            try {
                if (cpuReader != null) {
                    cpuReader.close();
                }
                if (pidReader != null) {
                    pidReader.close();
                }
            } catch (IOException exception) {
                Log.e(TAG, "doSample: ", exception);
            }
        }
    }

    void reset() {
        mUserLast = 0;
        mSystemLast = 0;
        mIdleLast = 0;
        mIoWaitLast = 0;
        mTotalLast = 0;
        mAppCpuTimeLast = 0;
        mCpuInfoEntries.clear();
    }

    private void parse(String cpuRate, String pidCpuRate) {
        String[] cpuInfoArray = cpuRate.split(" ");
        if (cpuInfoArray.length < 9) {
            return;
        }

        long user = Long.parseLong(cpuInfoArray[2]);
        long nice = Long.parseLong(cpuInfoArray[3]);
        long system = Long.parseLong(cpuInfoArray[4]);
        long idle = Long.parseLong(cpuInfoArray[5]);
        long ioWait = Long.parseLong(cpuInfoArray[6]);
        long total = user + nice + system + idle + ioWait
                + Long.parseLong(cpuInfoArray[7])
                + Long.parseLong(cpuInfoArray[8]);

        String[] pidCpuInfoList = pidCpuRate.split(" ");
        if (pidCpuInfoList.length < 17) {
            return;
        }

        long appCpuTime = Long.parseLong(pidCpuInfoList[13])
                + Long.parseLong(pidCpuInfoList[14])
                + Long.parseLong(pidCpuInfoList[15])
                + Long.parseLong(pidCpuInfoList[16]);

        if (mTotalLast != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            long idleTime = idle - mIdleLast;
            long totalTime = total - mTotalLast;

            if(totalTime != 0) {
                stringBuilder
                        .append("cpu:")
                        .append((totalTime - idleTime) * 100L / totalTime)
                        .append("% ")
                        .append("app:")
                        .append((appCpuTime - mAppCpuTimeLast) * 100L / totalTime)
                        .append("% ")
                        .append("[")
                        .append("user:").append((user - mUserLast) * 100L / totalTime)
                        .append("% ")
                        .append("system:").append((system - mSystemLast) * 100L / totalTime)
                        .append("% ")
                        .append("ioWait:").append((ioWait - mIoWaitLast) * 100L / totalTime)
                        .append("% ]");

                mCpuInfoEntries.put(System.currentTimeMillis(), stringBuilder.toString());
                if (mCpuInfoEntries.size() > MAX_ENTRY_COUNT) {
                    for (Map.Entry<Long, String> entry : mCpuInfoEntries.entrySet()) {
                        Long key = entry.getKey();
                        mCpuInfoEntries.remove(key);
                        break;
                    }
                }
            }
        }
        mUserLast = user;
        mSystemLast = system;
        mIdleLast = idle;
        mIoWaitLast = ioWait;
        mTotalLast = total;

        mAppCpuTimeLast = appCpuTime;
    }
}