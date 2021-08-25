package com.dongnao.dnskin.skin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author Lance
 * @date 2018/3/19
 */
public class SkinUtils {

    /**
     * 获取一个文件的md5值(可处理大文件)
     *
     * @return md5 value
     */
    public static String getSkinMD5(File file) {
        FileInputStream fis = null;
        BigInteger bi = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[10240];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            byte[] digest = MD5.digest();
            bi = new BigInteger(1, digest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bi.toString(16);
    }


}
