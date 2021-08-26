/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileAccessStrategy.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileAccessStrategy
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess;


import android.os.Environment;

import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;
import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;


public class FileAccessStrategy {


    @FileConstants.FileType
    public static int getOpenFileType(String filePath) {
        if (!isSandbox() || isPrivateDir(filePath)) {
            return FileConstants.FileType.TYPE_FILE;
        } else {
            return FileConstants.FileType.TYPE_MEDIA_STORE;
        }
    }

    @FileConstants.FileType
    public static int getCreateFileType(String filePath) {
        if (!isSandbox() ) {
            return FileConstants.FileType.TYPE_FILE;
        } else if (filePath.contains("Download")) {
            return FileConstants.FileType.TYPE_MEDIA_STORE;
        } else {
            return FileConstants.FileType.TYPE_SAF;
        }
    }

    @FileConstants.FileType
    public static int getMoveFileType(String srcFilePath, String targetFilePath) {
        if (!isSandbox()) {
            return FileConstants.FileType.TYPE_FILE;
        } else if (isPrivateDir(targetFilePath) && isPrivateDir(srcFilePath)) {
            return FileConstants.FileType.TYPE_FILE;
        } else {
            return FileConstants.FileType.TYPE_MEDIA_STORE;
        }
    }

    @FileConstants.FileType
    public static int getCopyFileType(String srcFilePath, String targetFilePath) {
        if (!isSandbox()) {
            return FileConstants.FileType.TYPE_FILE;
        } else if (isPrivateDir(targetFilePath) && isPrivateDir(srcFilePath)) {
            return FileConstants.FileType.TYPE_FILE;
        }  else {
            return FileConstants.FileType.TYPE_MEDIA_STORE;
        }

    }

    @FileConstants.FileType
    public static int getDeleteFileType(String filePath) {
        if (!isSandbox() || isPrivateDir(filePath)) {
            return FileConstants.FileType.TYPE_FILE;
        } else {
            return FileConstants.FileType.TYPE_MEDIA_STORE;
        }
    }

    private static boolean isPrivateDir(String filePath) {
        return OppoEnvironment.isPrivateDir(filePath);
    }

    private static boolean isPublicDir(String filePath) {
        return OppoEnvironment.isPublicDir(filePath);
    }

    private static boolean isUserSAF(String filePath) {
        return OppoEnvironment.isOtherDir(filePath);
    }

    private static boolean isSandbox() {
        return !Environment.isExternalStorageLegacy();
    }
}
