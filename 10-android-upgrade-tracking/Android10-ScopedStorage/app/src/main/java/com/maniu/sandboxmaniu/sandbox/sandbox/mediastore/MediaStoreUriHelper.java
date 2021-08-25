/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : MediaStoreUriHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          MediaStoreUriHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.CopyFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.RenameToFileRequest;

import java.io.File;

public class MediaStoreUriHelper {
    private static final String TAG = "MediaStoreUriHelper";
    private static final String IMAGES = "images";

    /**
     * getInsertContentUri
     *
     * @param request NewFileRequest
     * @return contentUri
     * content://media/volumeName/images/media
     * content://media/volumeName/video/media
     * content://media/volumeName/files
     * content://media/volumeName/download
     */
    public static Uri getInsertContentUri(NewFileRequest request) {
        if (request == null) {
            return null;
        }
        Uri uri = request.getContentUri();
        if (uri == null) {
            String filePath = request.getFilePath();
            boolean isImage = isImageFile(request.isImage(), filePath);
            uri = getInsertContentUri(filePath, isImage);
        }
        return uri;
    }

    /**
     * @param context Context
     * @param request DeleteFileRequest
     * @return Uri
     * content://media/volumeName/images/media/123
     * content://media/volumeName/video/media/123
     */
    public static Uri getUri(Context context, DeleteFileRequest request) {
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();
        if (uri == null) {
            File file = request.getFile();
            boolean isImageFile = isImageFile(request.isImage(), file.getAbsolutePath());
            uri = getMediaUri(context, file, request.getMediaId(), isImageFile);
        }
        return uri;
    }


    /**
     * @param context Context
     * @param request DeleteFileRequest
     * @return Uri
     * content://media/volumeName/images/media/123
     * content://media/volumeName/video/media/123
     */
    public static Uri getUri(Context context, OpenFileRequest request) {
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();
        if (uri == null) {
            File file = request.getFile();
            boolean isImageFile = isImageFile(request.isImage(), file.getAbsolutePath());
            uri = getMediaUri(context, file, request.getMediaId(), isImageFile);
        }
        return uri;
    }


    /**
     * @param context Context
     * @param request DeleteFileRequest
     * @param context Context
     * @param request CopyFileRequest
     * @return Uri
     * content://media/volumeName/images/media/123
     * content://media/volumeName/video/media/123
     */
    public static Uri getUri(Context context, CopyFileRequest request) {
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();
        if (uri == null) {
            File srcFile = request.getSrcFile();
            boolean isImage = isImageFile(request.isImage(), srcFile.getAbsolutePath());
            uri = getMediaUri(context, srcFile, request.getMediaId(), isImage);
        }
        return uri;

    }

    /**
     * @param context Context
     * @param request RenameToFileRequest
     * @return Uri content://media/volumeName/images/media/123 ||   content://media/volumeName/video/media/123
     */
    public static Uri getUri(Context context, RenameToFileRequest request) {
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();
        if (uri == null) {
            File srcFile = request.getSrcFile();
            boolean isImage = isImageFile(request.isImage(), srcFile.getAbsolutePath());
            uri = getMediaUri(context, srcFile, request.getMediaId(), isImage);
        }
        return uri;
    }


    /**
     * 获取 图片或者视频的uri
     *
     * @param context  context
     * @param filePath filePath
     * @param mediaId  mediaId
     * @param isImage  isImage
     * @return Uri content://media/volumeName/images/media/123 ||   content://media/volumeName/video/media/123
     */

    public static Uri getMediaUri(Context context, String filePath, String mediaId, boolean isImage) {
        Uri baseUri = getMediaContentUri(filePath, isImage);
        if (TextUtils.isEmpty(mediaId)) {
            mediaId = getMediaId(context, filePath, isImage);
        }
        Uri uri = null;
        if (!TextUtils.isEmpty(mediaId)) {
            uri = getUri(baseUri, mediaId);
        }
        return uri;
    }

    /**
     * 获取图片或者视频的uri
     *
     * @param context context
     * @param file    File
     * @param mediaId mediaId
     * @param isImage isImage
     * @return Uri content://media/volumeName/images/media/123 ||content://media/volumeName/video/media/123
     */
    public static Uri getMediaUri(Context context, File file, String mediaId, boolean isImage) {
        String filePath = file.getAbsolutePath();
        return getMediaUri(context, filePath, mediaId, isImage);
    }

    /**
     * 获取图片或者视频的基础uri
     *
     * @param filePath File
     * @param isImage  true or false
     * @return content://media/volumeName/images/media ||content://media/volumeName/video/media
     */
    public static Uri getMediaContentUri(String filePath, boolean isImage) {
        return isImage ? getImageContentUri(filePath) : getVideoContentUri(filePath);
    }

    /**
     * 获取uri
     *
     * @param baseUri baseUri
     *                content://media/volumeName/images/media
     *                content://media/volumeName/video/media
     *                content://media/volumeName/files
     *                content://media/volumeName/download
     * @param mediaId mediaId
     * @return UriUri
     * content://media/volumeName/images/media/123
     * content://media/volumeName/video/media/123
     * content://media/volumeName/files/123
     * content://media/volumeName/download/123
     */
    public static Uri getUri(Uri baseUri, String mediaId) {
        return Uri.withAppendedPath(baseUri, mediaId);
    }

    /**
     * @param filePath filePath
     * @param isImage  isImage
     * @return contentUri
     * content://media/volumeName/images/media
     * content://media/volumeName/video/media
     * content://media/volumeName/files
     * content://media/volumeName/download
     */
    public static Uri getInsertContentUri(String filePath, boolean isImage) {
        String relativePath = OppoEnvironment.getRelativePath(filePath);
        Uri uri = null;
        if (OppoEnvironment.isRelativeDCIMDir(relativePath)) {
            if (isImage) {
                uri = MediaStoreUriHelper.getImageContentUri(filePath);
            } else {
                uri = MediaStoreUriHelper.getVideoContentUri(filePath);
            }
        } else if (OppoEnvironment.isRelativePicturesDir(relativePath)) {
            uri = MediaStoreUriHelper.getImageContentUri(filePath);
        } else if (OppoEnvironment.isRelativeMoviesDir(relativePath)) {
            uri = MediaStoreUriHelper.getVideoContentUri(filePath);
        } else if (OppoEnvironment.isRelativeDocumentsDir(relativePath)) {
            uri = MediaStoreUriHelper.getFileContentUri(filePath);
        } else if (OppoEnvironment.isRelativeDownloadsDir(relativePath)) {
            uri = MediaStoreUriHelper.getDownloadContentUri(filePath);
        } else {
            uri = MediaStoreUriHelper.getFileContentUri(filePath);
        }
        return uri;
    }


    public static Uri getImageContentUri(String filePath) {
        if (filePath == null) {
            return null;
        }
        String volumeName = OppoEnvironment.getVolumeName(filePath);
        return MediaStore.Images.Media.getContentUri(volumeName);
    }


    public static Uri getVideoContentUri(String filePath) {
        if (filePath == null) {
            return null;
        }
        String volumeName = OppoEnvironment.getVolumeName(filePath);
        return MediaStore.Video.Media.getContentUri(volumeName);
    }


    public static Uri getFileContentUri(String filePath) {
        if (filePath == null) {
            return null;
        }
        String volumeName = OppoEnvironment.getVolumeName(filePath);
        return MediaStore.Files.getContentUri(volumeName);
    }


    public static Uri getDownloadContentUri(String filePath) {
        if (filePath == null) {
            return null;
        }
        String volumeName = OppoEnvironment.getVolumeName(filePath);
        return MediaStore.Downloads.getContentUri(volumeName);
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

    private static boolean isImageFile(Boolean isImageFile, String filePath) {
        boolean isImage = false;
        if (isImageFile == null) {
            isImage = !isVideo(filePath);
        } else {
            isImage = isImageFile;
        }
        return isImage;
    }


    private static String getMediaId(Context context, String filePath, boolean isImage) {
        long time = System.currentTimeMillis();

        Uri baseUri = getMediaContentUri(filePath, isImage);
        String[] project = {MediaStore.MediaColumns._ID};
        Cursor cursor = context.getContentResolver().query(baseUri,
                project, MediaStore.Images.Media.DATA + " = ? ",
                new String[]{filePath}, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToNext();
            String mediaId = cursor.getString(0);
            cursor.close();
            return mediaId;
        }
        return null;
    }

    public static boolean isImageUri(Uri uri) {
        return uri.toString().contains(IMAGES);
    }


}
