package me.ztiany.mmkv.simulation;

import org.jetbrains.annotations.NotNull;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-07-24 15:54
 */
public class NativeBridge {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native void mmapWrite( );

    public native void mmapInit(@NotNull String absolutePath);

    public native void mmapRead();

    public native void destroy();

}
