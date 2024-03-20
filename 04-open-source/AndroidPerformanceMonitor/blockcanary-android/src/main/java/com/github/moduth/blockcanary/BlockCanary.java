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
package com.github.moduth.blockcanary;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.moduth.blockcanary.ui.DisplayActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;

public final class BlockCanary {

    private static final String TAG = "BlockCanary";

    private static BlockCanary sInstance;
    private BlockCanaryInternals mBlockCanaryCore;
    private boolean mMonitorStarted = false;

    private BlockCanary() {
        Log.d(TAG, "BlockCanary()");
        BlockCanaryInternals.setContext(BlockCanaryContext.get());
        mBlockCanaryCore = BlockCanaryInternals.getInstance();
        mBlockCanaryCore.addBlockInterceptor(BlockCanaryContext.get());
        if (!BlockCanaryContext.get().displayNotification()) {
            return;
        }
        mBlockCanaryCore.addBlockInterceptor(new DisplayService());
    }

    /**
     * Install {@link BlockCanary}
     *
     * @param context            Application context
     * @param blockCanaryContext BlockCanary context
     * @return {@link BlockCanary}
     */
    public static BlockCanary install(Context context, BlockCanaryContext blockCanaryContext) {
        BlockCanaryContext.init(context, blockCanaryContext);
        setEnabled(context, DisplayActivity.class, BlockCanaryContext.get().displayNotification());
        return get();
    }

    /**
     * Get {@link BlockCanary} singleton.
     *
     * @return {@link BlockCanary} instance
     */
    public static BlockCanary get() {
        if (sInstance == null) {
            synchronized (BlockCanary.class) {
                if (sInstance == null) {
                    sInstance = new BlockCanary();
                }
            }
        }
        return sInstance;
    }

    /**
     * Start monitoring.
     */
    public void start() {
        if (!mMonitorStarted) {
            mMonitorStarted = true;
            Looper.getMainLooper().setMessageLogging(mBlockCanaryCore.monitor);
        }
    }

    /**
     * Stop monitoring.
     */
    public void stop() {
        if (mMonitorStarted) {
            mMonitorStarted = false;
            Looper.getMainLooper().setMessageLogging(null);
            mBlockCanaryCore.stackSampler.stop();
            mBlockCanaryCore.cpuSampler.stop();
        }
    }

    /**
     * Zip and upload log files, will user context's zip and log implementation.
     */
    public void upload() {
        Uploader.zipAndUpload();
    }

    /**
     * Record monitor start time to preference, you may use it when after push which tells start
     * BlockCanary.
     */
    public void recordStartTime() {
        PreferenceManager.getDefaultSharedPreferences(BlockCanaryContext.get().provideContext())
                .edit()
                .putLong("BlockCanary_StartTime", System.currentTimeMillis())
                .commit();
    }

    /**
     * Is monitor duration end, compute from recordStartTime end provideMonitorDuration.
     *
     * @return true if ended
     */
    public boolean isMonitorDurationEnd() {
        long startTime = PreferenceManager.getDefaultSharedPreferences(BlockCanaryContext.get().provideContext()).getLong("BlockCanary_StartTime", 0);
        return startTime != 0 && System.currentTimeMillis() - startTime > BlockCanaryContext.get().provideMonitorDuration() * 3600 * 1000;
    }

    // these lines are originally copied from LeakCanary: Copyright (C) 2015 Square, Inc.
    private static final Executor fileIoExecutor = newSingleThreadExecutor("File-IO");

    /**
     * 当某个 Activity 被禁用时，它将不能被启动。具体来说，这意味着：
     *
     * <ol>
     * <li>无法通过 Intent 启动：尝试从应用程序内部或其他应用通过 Intent 启动该 Activity 将不会成功。例如，使用 startActivity() 方法试图启动被禁用的 Activity 时，系统不会对该请求作出响应，该 Activity 不会被启动。</li>
     * <li>从 Launcher 中消失：如果该 Activity 在应用的 manifest 文件中被声明为一个入口点（即具有 <intent-filter> 包含 MAIN 动作和 LAUNCHER 类别），那么一旦被禁用，它将不再出现在设备的 Launcher 或应用抽屉中。这意味着用户不能从主屏幕启动该 Activity。</li>
     * <li>广播接收者（BroadcastReceiver）不会影响：仅仅禁用一个 Activity 不会影响应用内的其他组件，比如服务（Service）或广播接收者（BroadcastReceiver）。只有明确被禁用的组件才会受到影响。</li>
     * <li>静默失败：当尝试启动被禁用的 Activity 时，通常不会出现任何错误或异常。系统简单地不执行启动操作，这可能会使得调试变得复杂，因为没有明显的错误提示。</li>
     * <li>通过编程方式可以再次启用：即便一个 Activity 被禁用，应用程序仍然可以通过编程方式（如上文提供的 setEnabledBlocking 方法）重新启用该 Activity。这为动态控制应用组件提供了灵活性。</li>
     * </ol>
     * 禁用 Activity 是一种重要的应用管理手段，可以用于控制应用的行为，比如根据用户的选择启用或禁用某些功能、或者在应用的不同版本之间启用/禁用特定的功能等。然而，这种做法应谨慎使用，因为它会直接影响用户的体验和应用的可用性。在禁用任何组件前，应该确保这一操作对用户来说是透明的，或者至少能够提供适当的反馈。
     */
    private static void setEnabledBlocking(
            Context appContext,
            Class<?> componentClass,
            boolean enabled
    ) {
        ComponentName component = new ComponentName(appContext, componentClass);
        PackageManager packageManager = appContext.getPackageManager();
        int newState = enabled ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED;
        // Blocks on IPC.
        packageManager.setComponentEnabledSetting(component, newState, DONT_KILL_APP);
    }
    // end of lines copied from LeakCanary

    private static void executeOnFileIoThread(Runnable runnable) {
        fileIoExecutor.execute(runnable);
    }

    private static Executor newSingleThreadExecutor(String threadName) {
        return Executors.newSingleThreadExecutor(new SingleThreadFactory(threadName));
    }

    private static void setEnabled(Context context,
                                   final Class<?> componentClass,
                                   final boolean enabled) {
        final Context appContext = context.getApplicationContext();
        executeOnFileIoThread(new Runnable() {
            @Override
            public void run() {
                setEnabledBlocking(appContext, componentClass, enabled);
            }
        });
    }

}