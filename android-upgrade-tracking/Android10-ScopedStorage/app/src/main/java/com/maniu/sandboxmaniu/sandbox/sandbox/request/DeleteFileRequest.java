/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : DeleteFileRequest.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          DeleteFileRequest
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.request;

import android.net.Uri;

import java.io.File;


public class DeleteFileRequest {
    /**
     * 需要删除的文件： 必传参数
     */
    private File mFile;

    /**
     * 需要删除文件是否为图片
     * 沙箱模式下，媒体库删除文件的时候 必须要指定uri类型
     * 如果是图片，则必须用  image的uri ：MediaStore.Images.Media.EXTERNAL_CONTENT_URI
     * 如果是视频，则必须用 video的uri ：MediaStore.Video.Media.EXTERNAL_CONTENT_URI
     * 不能用file类型的uri
     * <p>
     * 非必传参数，
     * <p>
     * 如有调用处能够知道是图片还是视频，则尽量传入该参数，
     * 否则后续需要通过接口来获取，有一定的耗时
     */
    private Boolean mIsImage;

    /**
     * 删除文件的完整uri
     * 沙箱上删除文件，必须指定文件的完整uri
     * 图片：content://media/volumeName/images/media/123
     * 视频：content://media/volumeName/video/media/123
     * <p>
     * 非必传参数
     * 如果调用处能拿到uri，尽量传入，否则后面需要通过filePath和mediaId 去获取
     */
    private Uri mUri;

    /**
     * 文件的 mediaId
     * 沙箱上删除文件，必须指定文件的完整uri
     * 通过 Uri.withAppendedPath(baseUri, mMediaId)  去拼接完整uri
     * 非必传参数：如果已经传入了Uri 则不需要传入该值。
     */
    private String mMediaId;

    /**
     * 构建默认的 request
     *
     * @param file File
     * @return DeleteFileRequest
     */
    public static DeleteFileRequest buildRequest(File file) {
        return new DeleteFileRequest.Builder().setFile(file).builder();
    }

    public DeleteFileRequest(Builder builder) {
        mFile = builder.mFile;
        mIsImage = builder.mIsImage;
        mMediaId = builder.mMediaId;
        mUri = builder.mUri;
    }

    public File getFile() {
        return mFile;
    }

    public Boolean isImage() {
        return mIsImage;
    }

    public Uri getUri() {
        return mUri;
    }

    public String getMediaId() {
        return mMediaId;
    }

    public String getFilePath() {
        return mFile.getAbsolutePath();
    }

    @Override
    public String toString() {
        return "DeleteFileRequest{" +
                "mFile=" + mFile +
                ", mIsImage=" + mIsImage +
                ", mUri=" + mUri +
                ", mMediaId='" + mMediaId + '\'' +
                '}';
    }

    public static class Builder {
        private File mFile;
        private Boolean mIsImage;
        private Uri mUri;
        private String mMediaId;

        public DeleteFileRequest.Builder setFile(File file) {
            mFile = file;
            return this;
        }

        public DeleteFileRequest.Builder setImage(Boolean image) {
            mIsImage = image;
            return this;
        }

        public DeleteFileRequest.Builder setUri(Uri uri) {
            mUri = uri;
            return this;
        }

        public DeleteFileRequest.Builder setMediaId(String mediaId) {
            mMediaId = mediaId;
            return this;
        }

        public DeleteFileRequest builder() {
            return new DeleteFileRequest(this);
        }
    }

}
