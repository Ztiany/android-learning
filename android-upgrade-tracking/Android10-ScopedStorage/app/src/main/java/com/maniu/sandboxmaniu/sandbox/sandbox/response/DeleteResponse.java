/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileResponse.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileResponse
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.response;

import android.net.Uri;

import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;

import java.io.File;


public class DeleteResponse {
    /**
     * success or error
     */
    private boolean mIsSuccess;
    private boolean isNoPermission = false;
    /**
     * MediaStore uri
     * create file return
     * 创建文件返回的媒体库文件uri，后续通过写入文件内容需要用到。
     * <p>
     * content://media/volumeName/images/media/123
     * content://media/volumeName/video/media/123
     * content://media/volumeName/files/123
     * content://media/volumeName/download/123
     */
    private Uri mUri;

    /**
     * 文件操作类型：
     */
    private int mFileAccessType;

    private File mFile;

    public void setSuccess(boolean success) {
        mIsSuccess = success;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public void setFileAccessType(int fileAccessType) {
        mFileAccessType = fileAccessType;
    }

    public void setFile(File file) {
        mFile = file;
    }

    public File getFile() {
        return mFile;
    }

    public DeleteResponse(Builder builder) {
        mIsSuccess = builder.mIsSuccess;
        mUri = builder.mUri;
        mFileAccessType = builder.mAccessType;
        mFile = builder.mFile;
    }

    public boolean isFileType() {
        return mFileAccessType == FileConstants.FileType.TYPE_FILE;
    }

    public boolean isMediaStoreType() {
        return mFileAccessType == FileConstants.FileType.TYPE_MEDIA_STORE;
    }

    public boolean isSAFType() {
        return mFileAccessType == FileConstants.FileType.TYPE_SAF;
    }

    public boolean isSuccess() {
        return mIsSuccess;
    }

    public Uri getUri() {
        if (isMediaStoreType() || isSAFType()) {
            return mUri;
        }
        return null;
    }

    @Override
    public String toString() {
        return "FileResponse{" +
                "mIsSuccess=" + mIsSuccess +
                ", mUri=" + mUri +
                ", mFileAccessType=" + mFileAccessType +
                ", mFile=" + mFile +
                '}';
    }

    public static class Builder {
        private boolean mIsSuccess = false;
        private Uri mUri = null;
        private int mAccessType;
        private File mFile;

        public Builder setSuccess(boolean isSuccess) {
            mIsSuccess = isSuccess;
            return this;
        }

        public Builder setUri(Uri uri) {
            mUri = uri;
            return this;
        }

        public Builder setFile(File file) {
            mFile = file;
            return this;
        }

        public Builder setAccessType(int accessType) {
            mAccessType = accessType;
            return this;
        }

        public DeleteResponse builder() {
            return new DeleteResponse(this);
        }

    }
}
