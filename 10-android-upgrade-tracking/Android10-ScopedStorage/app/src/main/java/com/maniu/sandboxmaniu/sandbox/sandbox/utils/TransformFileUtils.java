/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : TransformFileUtils.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          TransformFileUtils
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.utils;


import android.os.Environment;

import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;

import java.io.File;


public class TransformFileUtils {
    private static final String TAG = "TransformFileUtils";
    private static final String GALLERY_TRANSFORM = "/gallery/transform/";
    private static final String PICTURE_GALLERY = OppoEnvironment.DIR_PICTURES + GALLERY_TRANSFORM;
    private static final String VIDEO_GALLERY = OppoEnvironment.DIR_MOVIES + GALLERY_TRANSFORM;

    /**
     * 目录转换
     * 1.图片:文件是图片，但是目录不是DCIM或者Pictures ,则把目录转换成 /Pictures/gallery/transform/xxxxx";
     * 2.视频:文件是视频，但是目录不是DCIM或者Movies , 则把目录转换成 /Movies/gallery/transform/xxxxx";
     * 涉及到的场景为：保存文件的时候，使用。
     * 需要转换的路径最后由产品提供
     *
     * @param file    file
     * @param isImage 是否为图片
     * @return File
     */
    public static File fileTransform(File file, Boolean isImage) {
        long time = System.currentTimeMillis();
        boolean isSandbox = !Environment.isExternalStorageLegacy();
        File newFile = null;
        String filePath = file.getAbsolutePath();
        if (isSandbox  ) {
            String displayName = OppoEnvironment.getDisplayName(filePath);
            String relativePath = filePath;
            int relativePathLen = relativePath.length();
            int filePathLen = filePath.length();
            String rootPath = filePath.substring(0, filePathLen - relativePathLen);
            if (isImage == null) {
                isImage = isImageType(filePath);
            }
            if (isImage) {
                if (!OppoEnvironment.isImageRelativeDir(relativePath)) {
                    String newFilePath = rootPath + PICTURE_GALLERY + relativePath;
                    newFile = new File(newFilePath);
                } else {
                    newFile = file;
                }
            } else {
                if (!OppoEnvironment.isVideoRelativeDir(relativePath)) {
                    String newFilePath = rootPath + VIDEO_GALLERY + relativePath;
                    newFile = new File(newFilePath);
                } else {
                    newFile = file;
                }
            }
        } else {
            newFile = file;
        }

        return newFile;
    }

    private static boolean isImageType(String filePath) {
       /* MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(filePath);
        if (mediaFileType == null) {
            return false;
        }
        return MediaFile.isImageFileType(mediaFileType.fileType);*/
       return !isVideo(filePath);
    }

    public static boolean isVideo(String path) {
        if (path.endsWith(".mp4")){
            return true;
        }else {
            return false;
        }
        /*MediaFile.MediaFileType mediaFileType = MediaFile.getFileType(path);
        if (mediaFileType == null) {
            return false;
        }
        boolean result = MediaFile.isVideoFileType(mediaFileType.fileType);
        Debugger.d(TAG, "isVideo path = " + path + ",result = " + result);
        return result;*/
    }
}
