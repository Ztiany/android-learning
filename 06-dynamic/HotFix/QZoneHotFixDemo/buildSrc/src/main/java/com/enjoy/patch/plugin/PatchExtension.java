package com.enjoy.patch.plugin;

/**
 * 插件的配置
 */
public class PatchExtension {

    /**
     * 在 debug 模式下，也开启插桩
     */
    boolean debugOn;

    /**
     * 补丁的输出位置
     */
    String output;

    /**
     * 应用的 application 类名
     */
    String applicationName;

    public PatchExtension() {
        debugOn = false;
    }

    public void setDebugOn(boolean debugOn) {
        this.debugOn = debugOn;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

}
