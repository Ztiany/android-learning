/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : OpenFileRequest.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          OpenFileRequest
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.request;

import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;

import java.io.File;


public class OpenFileRequest {
    /**
     * 需要打开的文件  如果是File接口打开 则必传
     * 如果是媒体库接口打开文件，如果传入了Uri，则不需要传入，否则需要传入。
     */
    private File mFile;

    /**
     * 打开的文件是否为图片
     * 沙箱模式下，媒体库打开文件的时候 必须要指定uri类型
     * 如果是图片，则必须用  image的uri ：MediaStore.Images.Media.EXTERNAL_CONTENT_URI
     * 如果是视频，则必须用  video的uri ：MediaStore.Video.Media.EXTERNAL_CONTENT_URI
     * 不能用file类型的uri
     * <p>
     * 非必传参数，
     * <p>
     * 如有调用处能够知道是图片还是视频，则尽量传入该参数，
     * 否则后续需要通过接口来获取，有一定的耗时
     */
    private Boolean mIsImage;
    /**
     * 打开图片的模式
     * 是读还是写，如果不传，默认为只读
     */
    private int mModeType;


    /**
     * 打开文件的完整uri
     * 沙箱上打开文件，必须指定文件的完整uri
     * 图片：content://media/volumeName/images/media/123
     * 视频：content://media/volumeName/video/media/123
     * <p>
     * 非必传参数
     * 如果调用处能拿到uri，尽量传入，否则后面需要通过filePath和mediaId 去获取
     */
    private Uri mUri;

    /**
     * 文件的 mediaId
     * 沙箱上打开文件，必须指定文件的完整uri
     * 通过 Uri.withAppendedPath(baseUri, mMediaId)  去拼接完整uri
     * 非必传参数：如果已经传入了Uri 则不需要传入该值。
     */
    private String mMediaId;

    public OpenFileRequest(Builder builder) {
        mFile = builder.mFile;
        mIsImage = builder.mIsImage;
        mModeType = builder.mModeType;
        mMediaId = builder.mMediaId;
        mUri = builder.mUri;
    }

    /**
     * 构建必要参数的 request
     *
     * @param file File
     * @return NewFileRequest
     */
    public static OpenFileRequest buildRequest(File file) {
        return new OpenFileRequest.Builder().setFile(file).builder();
    }

    @Override
    public String toString() {
        return "OpenFileRequest{" +
                "mFile=" + mFile +
                ", mIsImage=" + mIsImage +
                ", mModeType=" + mModeType +
                ", mUri=" + mUri +
                ", mMediaId='" + mMediaId + '\'' +
                '}';
    }

    public static class Builder {
        private File mFile;
        private int mModeType;
        private Boolean mIsImage;
        private Uri mUri;
        private String mMediaId;

        public Builder setFile(File file) {
            mFile = file;
            return this;
        }

        public Builder setModeType(int modeType) {
            mModeType = modeType;
            return this;
        }

        public Builder setImage(Boolean image) {
            mIsImage = image;
            return this;
        }

        public Builder setUri(Uri uri) {
            mUri = uri;
            return this;
        }

        public Builder setMediaId(String mediaId) {
            mMediaId = mediaId;
            return this;
        }

        public OpenFileRequest builder() {
            return new OpenFileRequest(this);
        }
    }


    public Uri getUri() {
        return mUri;
    }

    public String getMediaId() {
        return mMediaId;
    }

    public File getFile() {
        return mFile;
    }

    public Boolean isImage() {
        return mIsImage;
    }

    public String getFilePath() {
        return mFile.getAbsolutePath();
    }

    public String getMode() {
        String mode = FileConstants.FileMode.MODE_READ;
        switch (mModeType) {
            case FileConstants.FileModeType.MODE_READ:
                return FileConstants.FileMode.MODE_READ;
            case FileConstants.FileModeType.MODE_WRITE:
            case FileConstants.FileModeType.MODE_READ_AND_WRITE:
                return FileConstants.FileMode.MODE_WRITE;
            default:
                break;
        }
        return mode;
    }

    public int getOpenFdMode() {
        int mode = ParcelFileDescriptor.MODE_READ_ONLY;
        switch (mModeType) {
            case FileConstants.FileModeType.MODE_READ:
                return ParcelFileDescriptor.MODE_READ_ONLY;
            case FileConstants.FileModeType.MODE_WRITE:
                return ParcelFileDescriptor.MODE_WRITE_ONLY;
            case FileConstants.FileModeType.MODE_READ_AND_WRITE:
                return ParcelFileDescriptor.MODE_READ_WRITE;
            default:
                break;
        }
        return mode;
    }
}
