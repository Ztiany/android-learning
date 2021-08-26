
/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : MoveFileByMediaStoreHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          MoveFileByMediaStoreHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;


import com.maniu.sandboxmaniu.sandbox.sandbox.IOUtils;
import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;
import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.RenameToFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.response.FileResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MoveFileByMediaStoreHelper {
    private static final String TAG = "MoveFileByMediaStoreHelper";
    private final static int RESULT_SUCCESS = 0;

    public static FileResponse renameTo(Context context, RenameToFileRequest renameRequest) {
        if ((renameRequest == null)
                || (renameRequest.getSrcFile() == null)
                || (renameRequest.getTargetFile() == null)) {
            return null;
        }
        // get file
        File srcFile = renameRequest.getSrcFile();
        File targetFile = renameRequest.getTargetFile();

        // get filePath
        String srcFilePath = srcFile.getAbsolutePath();
        String targetFilePath = targetFile.getAbsolutePath();

        // getRelative
        String srcRelative = OppoEnvironment.getRelativePath(srcFilePath);
        String targetRelative = OppoEnvironment.getRelativePath(targetFilePath);
        if (isPublicToPublic(srcRelative, targetRelative)) {
            // 从公共目录移动到公共目录
            if (targetIsDownLoad(targetRelative)) {
                // 目标目录是 Download 目录
                return publicToDownloadOrDocuments(context, renameRequest, true);
            } else if (targetIsDocuments(targetRelative)) {
                // 目标目录是 Documents 目录
                return publicToDownloadOrDocuments(context, renameRequest, false);
            } else {
                // 目标目录是 DCIM  Pictures Movies目录
                return publicToPublicMedia(context, renameRequest, targetRelative);
            }

        } else if (isPublicToPrivate(srcRelative, targetRelative)) {
            // 从公共目录移动到私有目录
            return publicToPrivate(context, renameRequest);
        } else if (isPrivateToPublic(srcRelative, targetRelative)) {
            // 从私有目录移动到公共目录
            return privateToPublic(context, renameRequest);
        }
        return null;
    }

    /**
     * 公共目录下的文件移动到 Download 或者Documents 目录下
     * 1.目标文件如果存在，则先通过媒体库接口删除目标文件
     * 2.通过 MediaStore.Downloads.getContentUri(volumeName)  插入到 download目录 或者 MediaStore.Files.getContentUri(volumeName) 插入到Documents目录
     * 3.通过 contentResolver.openOutputStream(uri)  打开目标文件的 OutputStream
     * 4.通过 contentResolver.openInputStream(uri)   打开源文件的 inputStream
     * 5.InputStream OutputStream 读写操作
     * 6.通过媒体库接口删除源文件
     *
     * @param context       Context
     * @param renameRequest RenameToFileRequest
     * @param isDownload    是否为Download目录  true为Download false为documents
     * @return FileResponse FileResponse
     */
    private static FileResponse publicToDownloadOrDocuments(Context context,
                                                            RenameToFileRequest renameRequest,
                                                            boolean isDownload) {
        long time = System.currentTimeMillis();
        FileResponse fileAccessResponse = null;

        File srcFile = renameRequest.getSrcFile();
        File targetFile = renameRequest.getTargetFile();

        if (targetFile.exists()) {
            //1.目标文件如果存在，则先通过媒体库接口删除目标文件
            DeleteFileRequest deleteRequest = new DeleteFileRequest.Builder()
                    .setFile(targetFile)
                    .setImage(renameRequest.isImage())
                    .setMediaId(renameRequest.getMediaId())
                    .setUri(renameRequest.getUri())
                    .builder();
            DeleteFileByMediaStoreHelper.delete(context, deleteRequest);
        }
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {

            //打开源文件
            Uri srcUri = MediaStoreUriHelper.getUri(context, renameRequest);
            OpenFileRequest srcOpenRequest = new OpenFileRequest.Builder()
                    .setModeType(FileConstants.FileModeType.MODE_READ)
                    .setFile(srcFile)
                    .setUri(srcUri)
                    .builder();

            fileInputStream = (FileInputStream) OpenFileByMediaStoreHelper
                    .getInputStream(context, srcOpenRequest);


            String targetFilePath = targetFile.getAbsolutePath();
            Uri contentUri;
            if (isDownload) {
                contentUri = MediaStoreUriHelper.getDownloadContentUri(targetFilePath);
            } else {
                contentUri = MediaStoreUriHelper.getFileContentUri(targetFilePath);
            }
            Boolean isImageFile = renameRequest.isImage();
            boolean isImage = false;
            if (isImageFile == null) {
                isImage = !MediaStoreUriHelper.isVideo(srcFile.getAbsolutePath());
            } else {
                isImage = isImageFile;
            }
            //构建创建目标目录的Request
            NewFileRequest createRequest = new NewFileRequest.Builder()
                    .setFile(targetFile)
                    .setImage(isImage)
                    .setContentValues(renameRequest.getContentValues())
                    .setContentUri(contentUri)
                    .builder();
            //创建目标文件
            FileResponse response = CreateFileByMediaStoreHelper.newCreateFile(context, createRequest);
            if ((response != null) && (response.isSuccess())) {
                //写入目标文件
                Uri uri = response.getUri();
                OpenFileRequest openRequest = new OpenFileRequest.Builder()
                        .setModeType(FileConstants.FileModeType.MODE_WRITE)
                        .setUri(uri)
                        .builder();
                fileOutputStream = (FileOutputStream) OpenFileByMediaStoreHelper
                        .getOutStream(context, openRequest);
                if ((fileOutputStream != null) && (fileInputStream != null)) {
                    byte[] buf = new byte[1024];
                    while (fileInputStream.read(buf) != -1) {
                        fileOutputStream.write(buf);
                    }
                    // step 4 删除源文件
                    DeleteFileRequest deleteRequest = new DeleteFileRequest.Builder()
                            .setUri(srcUri)
                            .builder();
                    boolean result = DeleteFileByMediaStoreHelper.delete(context, deleteRequest);
                    response.setSuccess(result);
                    fileAccessResponse = response;
                }
            }
        } catch (Exception e) {
        } finally {
        }
        return fileAccessResponse;

    }


    /**
     * 公共的文件 移动到  公共的媒体文件目录  -- DCIM Pictures Movies
     * 直接通过 媒体库的接口 update 相对路径  MediaStore.MediaColumns.RELATIVE_PATH
     *
     * @param context        Context
     * @param renameRequest  RenameToFileRequest
     * @param relativeTarget relativeTarget
     * @return FileResponse
     */
    private static FileResponse publicToPublicMedia(Context context,
                                                    RenameToFileRequest renameRequest,
                                                    String relativeTarget) {
        long time = System.currentTimeMillis();
        int result = -1;
        Uri uri = MediaStoreUriHelper.getUri(context, renameRequest);
        if (uri != null) {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                ContentValues contentValues = renameRequest.getContentValues();
                if (contentValues == null) {
                    contentValues = new ContentValues();
                }
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeTarget);
                result = contentResolver.update(uri, contentValues, null, null);
            } catch (RecoverableSecurityException exception) {
                // access to permission
                MediaStorePermissionHelper.startIntentSenderForResult(context, exception, MediaStorePermissionHelper.REQUEST_CODE_UPDATE);
            }
        }
        FileResponse response = new FileResponse.Builder()
                .setSuccess(result == RESULT_SUCCESS)
                .setUri(uri)
                .setAccessType(FileConstants.FileType.TYPE_MEDIA_STORE)
                .builder();
        return response;
    }

    /**
     * 私有目录的文件移动到公共目录下
     * 1.目标文件存在，则先通过媒体库接口删除目标文件
     * 2.读取源文件的InputStream
     * 3.通过 通过媒体库接口插入一条记录（需要区分目录uri），返回一个uri
     * 4.通过contentResolver.openOutputStream(uri)  打开目标文件的 OutputStream
     * 5.目标文件读写流
     * 6.file接口删除源文件
     *
     * @param context Context
     * @param request RenameToFileRequest
     * @return FileResponse
     */
    private static FileResponse privateToPublic(Context context, RenameToFileRequest request) {
        long time = System.currentTimeMillis();
        File srcFile = request.getSrcFile();
        File targetFile = request.getTargetFile();

        FileResponse fileAccessResponse = null;
        // target file  exists ，delete target file
        if (targetFile.exists()) {
            DeleteFileRequest deleteRequest = new DeleteFileRequest.Builder()
                    .setFile(targetFile)
                    .setImage(request.isImage())
                    .setUri(request.getUri())
                    .setMediaId(request.getMediaId())
                    .builder();
            DeleteFileByMediaStoreHelper.delete(context, deleteRequest);
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            // step 1  read src file FileInputStream
            fileInputStream = new FileInputStream(srcFile );
            Boolean isImageFile = request.isImage();
            boolean isImage = false;
            if (isImageFile == null) {
                isImage = !MediaStoreUriHelper.isVideo(srcFile.getAbsolutePath());
            } else {
                isImage = isImageFile;
            }
            // step 2   create target file
            NewFileRequest createRequest = new NewFileRequest.Builder()
                    .setFile(targetFile)
                    .setContentValues(request.getContentValues())
                    .setImage(isImage)
                    .builder();

            FileResponse response = CreateFileByMediaStoreHelper
                    .newCreateFile(context, createRequest);

            if ((response != null) && (response.isSuccess())) {
                // step 3 write target file
                Uri uri = response.getUri();
                OpenFileRequest openRequest = new OpenFileRequest.Builder()
                        .setModeType(FileConstants.FileModeType.MODE_WRITE)
                        .setUri(uri).builder();
                fileOutputStream = (FileOutputStream) OpenFileByMediaStoreHelper
                        .getOutStream(context, openRequest);
                if (fileOutputStream != null) {
                    byte[] buf = new byte[1024];
                    while (fileInputStream.read(buf) != -1) {
                        fileOutputStream.write(buf);
                    }
                    // step 4 delete srcFile
                    boolean delete = srcFile.delete();
                    response.setSuccess(delete);
                    fileAccessResponse = response;
                }
            } else {

            }
        } catch (Exception e) {
        } finally {

            IOUtils.closeQuietly(fileInputStream, fileOutputStream);
        }
        return fileAccessResponse;

    }

    /**
     * 公共目录下的文件移动 到私有目录下
     * 1.目标文件存在，则先通过file接口删除目标文件
     * 2.在私有目录下创建文件。
     * 3.通过 contentResolver.openInputStream(uri)   打开源文件的 inputStream
     * 4.通过 OutPutStream 写入文件
     * 5.通过媒体库接口删除源文件
     *
     * @param context Context
     * @param request RenameToFileRequest
     * @return FileResponse
     */
    private static FileResponse publicToPrivate(Context context, RenameToFileRequest request) {
        long time = System.currentTimeMillis();
        boolean result = false;

        File srcFile = request.getSrcFile();
        File targetFile = request.getTargetFile();

        FileInputStream fileInputStream = null;
        FileOutputStream outputStream = null;
        try {
            // step 1
            boolean mkdirResult = true;
            boolean createNewFile;
            if (targetFile.exists()) {
                boolean delete = targetFile.delete();
            }

            File parentFile = targetFile.getParentFile();
            if ((parentFile != null) && (!parentFile.exists())) {
                mkdirResult = parentFile.mkdir();
            }
            createNewFile = targetFile.createNewFile();


            // step 2
            OpenFileRequest openRequest = new OpenFileRequest.Builder()
                    .setModeType(FileConstants.FileModeType.MODE_READ)
                    .setFile(srcFile)
                    .setImage(request.isImage())
                    .setMediaId(request.getMediaId())
                    .setUri(request.getUri())
                    .builder();
            if (mkdirResult && createNewFile) {
                fileInputStream = (FileInputStream) OpenFileByMediaStoreHelper
                        .getInputStream(context, openRequest);
                outputStream = new FileOutputStream(targetFile );
                if (fileInputStream != null) {
                    byte[] buf = new byte[1024];
                    while (fileInputStream.read(buf) != -1) {
                        outputStream.write(buf);
                    }
                    // step 3
                    DeleteFileRequest deleteRequest = new DeleteFileRequest.Builder()
                            .setFile(srcFile)
                            .setUri(request.getUri())
                            .setImage(request.isImage())
                            .setMediaId(request.getMediaId())
                            .builder();
                    result = DeleteFileByMediaStoreHelper.delete(context, deleteRequest);
                }
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(fileInputStream, outputStream);
        }
        return new FileResponse.Builder()
                .setAccessType(FileConstants.FileType.TYPE_MEDIA_STORE)
                .setSuccess(result)
                .builder();
    }


    /**
     * 目标目录是否为 Download目录
     *
     * @param targetRelative RelativePath
     * @return boolean
     */
    private static boolean targetIsDownLoad(String targetRelative) {
        return OppoEnvironment.isRelativeDownloadsDir(targetRelative);
    }


    /**
     * 目标目录是否为 Documents目录
     *
     * @param targetRelative RelativePath
     * @return boolean
     */
    private static boolean targetIsDocuments(String targetRelative) {
        return OppoEnvironment.isRelativeDocumentsDir(targetRelative);
    }

    /**
     * 从公共文件目录移动到 公共目录
     *
     * @param srcRelative    srcRelative
     * @param targetRelative targetRelative
     * @return boolean
     */
    private static boolean isPublicToPublic(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePublicDir(srcRelative))
                && (OppoEnvironment.isRelativePublicDir(targetRelative));
    }

    /**
     * 从公共文件目录移动到 私有目录 Android/data
     *
     * @param srcRelative    srcRelative
     * @param targetRelative targetRelative
     * @return boolean
     */
    private static boolean isPublicToPrivate(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePublicDir(srcRelative))
                && (OppoEnvironment.isRelativePrivateDir(targetRelative));
    }

    /**
     * 从私有目录移动到公共目录
     *
     * @param srcRelative    srcRelative
     * @param targetRelative targetRelative
     * @return boolean
     */
    private static boolean isPrivateToPublic(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePrivateDir(srcRelative))
                && (OppoEnvironment.isRelativePublicDir(targetRelative));
    }
}
