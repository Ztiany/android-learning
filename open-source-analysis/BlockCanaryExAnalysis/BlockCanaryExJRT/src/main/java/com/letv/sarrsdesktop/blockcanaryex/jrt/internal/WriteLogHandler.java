/*
 * Copyright (C) 2017 seiginonakama (https://github.com/seiginonakama).
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

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * author: zhoulei date: 2017/2/28.
 */
public class WriteLogHandler extends Handler {
    private static volatile WriteLogHandler sInstance;
    private static HandlerThread sHandlerThread = new HandlerThread("BlockCanaryExLogWriter");

    private WriteLogHandler(Looper looper) {
        super(looper);
    }

    public static WriteLogHandler getInstance() {
        if(sInstance == null) {
            synchronized (WriteLogHandler.class) {
                if(sInstance == null) {
                    sHandlerThread.start();
                    sInstance = new WriteLogHandler(sHandlerThread.getLooper());
                }
            }
        }
        return sInstance;
    }
}
