/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileAccessFactory.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileAccessFactory
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess;


import com.maniu.sandboxmaniu.sandbox.sandbox.SAF.SAFAccessImp;
import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.file.FileAccessImp;
import com.maniu.sandboxmaniu.sandbox.sandbox.mediastore.MediaStoreAccessImp;

public class FileAccessFactory {

    private static final String TAG = "FileAccessFactory";

    public static FileAccessInterface getCreateFileAccess(String filePath) {
        @FileConstants.FileType int type = FileAccessStrategy.getCreateFileType(filePath);
        return getFileAccess(type);
    }


    public static FileAccessInterface getOpenFileAccess(String filePath) {
        @FileConstants.FileType int type = FileAccessStrategy.getOpenFileType(filePath);
        return getFileAccess(type);
    }

    public static FileAccessInterface getDeleteFileAccess(String filePath) {
        @FileConstants.FileType int type = FileAccessStrategy.getDeleteFileType(filePath);
        return getFileAccess(type);
    }

    public static FileAccessInterface getMoveFileAccess(String srcFilePath, String targetFilePath) {
        @FileConstants.FileType int type = FileAccessStrategy.getMoveFileType(srcFilePath, targetFilePath);
        return getFileAccess(type);
    }

    public static FileAccessInterface getCopyFileAccess(String srcFilePath, String targetFilePath) {
        @FileConstants.FileType int type = FileAccessStrategy.getCopyFileType(srcFilePath, targetFilePath);
        return getFileAccess(type);
    }

    private static FileAccessInterface getFileAccess(@FileConstants.FileType int type) {
        FileAccessInterface fileAccessInterface = null;
        switch (type) {
            case FileConstants.FileType.TYPE_FILE:
                fileAccessInterface = FileAccessImp.getInstance();
                break;
            case FileConstants.FileType.TYPE_MEDIA_STORE:
                fileAccessInterface = MediaStoreAccessImp.getInstance();
                break;
            case FileConstants.FileType.TYPE_SAF:
                fileAccessInterface = SAFAccessImp.getInstance();
                break;
            default:
                break;

        }
        return fileAccessInterface;
    }


}
