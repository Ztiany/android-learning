package com.ztiany.bsdiff;

import java.io.File;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class Main {


    static {
        System.loadLibrary("jni/bsdiff_windows");
    }

    public static void main(String... args) {
        //拆分
        Main main = new Main();
        main.diff();
    }

    private void diff() {
        final String oldFilePath = "file/version1.apk";
        final String newFilePath = "file/version2.apk";
        final String patchFileStorePath = "file/new.patch";
        if (new File(oldFilePath).exists() && new File(newFilePath).exists()) {
            new JniUtils().doDiff(oldFilePath, newFilePath, patchFileStorePath);
        } else {
            System.out.println("文件不存在哦");
        }

    }


}
