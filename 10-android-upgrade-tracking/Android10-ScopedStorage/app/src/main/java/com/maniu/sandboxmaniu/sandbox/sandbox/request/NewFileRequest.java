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
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          NewFileRequest
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.request;

import android.content.ContentValues;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.maniu.sandboxmaniu.sandbox.sandbox.utils.TransformFileUtils;

import java.io.File;


public class NewFileRequest {
    /**
     * 需要删除的文件： 必传参数
     */
    private File mFile;
    /**
     * 必传参数
     * 因为沙箱上 创建文件有非常严格的限制。
     * 创建文件必须指定Uri类型
     */
    private Boolean mIsImage;

    /**
     * 非必传参数
     * ContentValues 新建文件的时候传入的ContentValues
     * 如果没有传入，接口会自动填充一些基本的数据
     */
    private ContentValues mContentValues;

    /**
     * 媒体库 uri  （不包含id）
     * <p>
     * 非必传参数，
     * 如果调用处能拿到Uir，尽量传入，
     * 否则接口会接口会根据 File去获取 uri,
     * {@link android.provider.MediaStore}
     * <p>
     * 沙箱上 在公共目录下创建文件限制非常严格
     * <p>
     * 1.DCIM ：只能插入图片和视频 因此uri 必须要
     * MediaStore.Images.Media.EXTERNAL_CONTENT_URI （图片）
     * MediaStore.Video.Media.EXTERNAL_CONTENT_URI（视频）
     * 2.Pictures：只能插入图片 Uri必须为：MediaStore.Images.Media.EXTERNAL_CONTENT_URI （图片）
     * 3.Movies :只能插入视频，Uri必须为：MediaStore.Video.Media.EXTERNAL_CONTENT_URI
     * 4.Downloads：可以插入任意文件，Uri必须为：
     * MediaStore.Downloads.getContentUri(volumeName)
     * MediaStore.Files.getContentUri(volumeName)
     * 5.Documents :可以插入任意文件，Uri必须为： MediaStore.Files.getContentUri(volumeName)
     */
    private Uri mContentUri;

    public NewFileRequest(Builder builder) {
        mFile = builder.mFile;
        mContentValues = builder.mContentValues;
        mIsImage = builder.mIsImageFile;
        mContentUri = builder.mContentUri;

    }
    public NewFileRequest(File file) {
        mFile = file;
    }
    @Override
    public String toString() {
        return "NewFileRequest{" +
                "mFile=" + mFile +
                ", mIsImage=" + mIsImage +
                ", mContentValues=" + mContentValues +
                ", mContentUri=" + mContentUri +
                '}';
    }

    public File getFile() {
        return TransformFileUtils.fileTransform(mFile, isImage());
    }

    public Boolean isImage() {
        return mIsImage;
    }

    public String getFilePath() {
        return getFile().getAbsolutePath();
    }

    public Uri getContentUri() {
        return mContentUri;
    }


    public ContentValues getContentValues() {
        return mContentValues;
    }


    /**
     * 构建必要参数的 request
     * @param file    File
     * @param isImage 是否为图片
     * @return NewFileRequest
     */
    public static NewFileRequest buildRequest(File file, Boolean isImage) {
        return new NewFileRequest.Builder().setFile(file).setImage(isImage).builder();
    }


    public static class Builder {
        private File mFile;
        private ContentValues mContentValues;
        private Boolean mIsImageFile;
        private Uri mContentUri;

        @NonNull
        public Builder setFile(File file) {
            mFile = file;
            return this;
        }


        public Builder setContentValues(ContentValues contentValues) {
            mContentValues = contentValues;
            return this;
        }

        public Builder setImage(Boolean imageFile) {
            mIsImageFile = imageFile;
            return this;
        }

        public Builder setContentUri(Uri uri) {
            mContentUri = uri;
            return this;
        }


        public NewFileRequest builder() {
            return new NewFileRequest(this);
        }
    }


}
