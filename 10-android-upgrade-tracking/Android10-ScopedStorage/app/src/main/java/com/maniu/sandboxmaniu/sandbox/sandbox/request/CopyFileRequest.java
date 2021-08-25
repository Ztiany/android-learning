
package com.maniu.sandboxmaniu.sandbox.sandbox.request;

import android.content.ContentValues;
import android.net.Uri;

import com.maniu.sandboxmaniu.sandbox.sandbox.utils.TransformFileUtils;

import java.io.File;


public class CopyFileRequest {
    /**
     * 源文件，必传参数
     */
    private File mSrcFile;

    /**
     * 目标文件，必传参数
     */
    private File mTargetFile;

    /**
     * 源文件是否为图片，复制的时候需要读写文件。涉及到文件的打开。
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
     * 源文件的完整uri
     * 沙箱上打开文件，必须指定文件的完整uri
     * 图片：content://media/volumeName/images/media/123
     * 视频：content://media/volumeName/video/media/123
     * <p>
     * 非必传参数
     * 如果调用处能拿到uri，尽量传入，否则后面需要通过filePath和mediaId 去获取
     */
    private Uri mUri;
    /**
     * 源文件的 mediaId
     * 沙箱上打开文件，必须指定文件的完整uri
     * 通过 Uri.withAppendedPath(baseUri, mMediaId)  去拼接完整uri
     * 非必传参数：如果已经传入了Uri 则不需要传入该值。
     */
    private String mMediaId;

    /**
     * 非必传参数，如果不传入，新建文件的时候，会默认填充一些默认数据。
     * 1.从 DCIM Pictures Movies 移动文件到 Documents和Download目录下，需要先复制文件，
     * 然后插入到Documents和Download目录下，因此可能需要mContentValues
     * 2.从私有目录移动到公共目录下，需要插入媒体库。，因此可能需要mContentValues
     */
    private ContentValues mContentValues;

    public CopyFileRequest(Builder builder) {
        mSrcFile = builder.mSrcFile;
        mIsImage = builder.mIsImage;
        mTargetFile = builder.mTargetFile;
        mMediaId = builder.mMediaId;
        mUri = builder.mUri;
        mContentValues = builder.mContentValues;
    }

    /**
     * 构建必要参数的 request
     *
     * @param srcFile    File
     * @param targetFile File
     * @return CopyFileRequest
     */
    public static CopyFileRequest buildRequest(File srcFile, File targetFile) {
        return new CopyFileRequest.Builder()
                .setSrcFile(srcFile)
                .setTargetFile(targetFile)
                .builder();
    }

    public static class Builder {
        private File mSrcFile;
        private Boolean mIsImage;
        private File mTargetFile;
        private String mMediaId;
        private Uri mUri;
        private ContentValues mContentValues;

        public Builder setSrcFile(File file) {
            mSrcFile = file;
            return this;
        }

        public Builder setImage(Boolean image) {
            mIsImage = image;
            return this;
        }

        public Builder setTargetFile(File targetFile) {
            mTargetFile = targetFile;
            return this;
        }

        public Builder setMediaId(String mediaId) {
            mMediaId = mediaId;
            return this;
        }

        public Builder setUri(Uri uri) {
            mUri = uri;
            return this;
        }

        public Builder setContentValues(ContentValues contentValues) {
            mContentValues = contentValues;
            return this;
        }

        public CopyFileRequest builder() {
            return new CopyFileRequest(this);
        }
    }

    public Boolean isImage() {
        return mIsImage;
    }

    public File getSrcFile() {
        return mSrcFile;
    }

    public File getTargetFile() {
        return TransformFileUtils.fileTransform(mTargetFile, isImage());
    }

    public String getMediaId() {
        return mMediaId;
    }

    public Uri getUri() {
        return mUri;
    }

    public ContentValues getContentValues() {
        return mContentValues;
    }

    public String getSrcFilePath() {
        return mSrcFile.getAbsolutePath();
    }

    public String getTargetFilePath() {
        return getTargetFile().getAbsolutePath();
    }

    @Override
    public String toString() {
        return "CopyFileRequest{" +
                "mSrcFile=" + mSrcFile +
                ", mTargetFile=" + mTargetFile +
                ", mIsImage=" + mIsImage +
                ", mUri=" + mUri +
                ", mMediaId='" + mMediaId + '\'' +
                ", mContentValues=" + mContentValues +
                '}';
    }
}
