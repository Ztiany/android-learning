package com.dn.lsn_12_demo;

public class ZipCode {

    static {
        System.loadLibrary("native-lib");
    }

    public native static int exec(String cmd);

}
