package com.ztiany.bsdiff;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class JniUtils {

    public native void doDiff(String oldFilePath, String newFilePatch, String patchFileStorePath);

}
