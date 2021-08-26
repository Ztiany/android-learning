
/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : CopyFileByMediaStoreHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          CopyFileByMediaStoreHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.Context;
import android.net.Uri;
import com.maniu.sandboxmaniu.sandbox.sandbox.IOUtils;
import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;
import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.CopyFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.response.FileResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CopyFileByMediaStoreHelper {
    private static final String TAG = "MoveFileByMediaStoreHelper";
    private final static int RESULT_SUCCESS = 0;

    public static boolean copyFile(Context context, CopyFileRequest copyFileRequest) {
        if ((copyFileRequest == null)
                || (copyFileRequest.getSrcFile() == null)
                || (copyFileRequest.getTargetFile() == null)) {
            return false;
        }
        // get file
        File srcFile = copyFileRequest.getSrcFile();
        File targetFile = copyFileRequest.getTargetFile();

        // get filePath
        String srcFilePath = srcFile.getAbsolutePath();
        String targetFilePath = targetFile.getAbsolutePath();

        // getRelative
        String srcRelative = OppoEnvironment.getRelativePath(srcFilePath);
        String targetRelative = OppoEnvironment.getRelativePath(targetFilePath);


        if (isPublicToPublic(srcRelative, targetRelative)) {
            // 从公共目录复制到公共目录
            return publicToPublic(context, copyFileRequest);
        } else if (isPublicToPrivate(srcRelative, targetRelative)) {
            // 从公共目录复制到私有目录
            return publicToPrivate(context, copyFileRequest);
        } else if (isPrivateToPublic(srcRelative, targetRelative)) {
            // 从私有目录复制到公共目录
            return privateToPublic(context, copyFileRequest);
        }
        return false;
    }



    /**
     * 私有目录的文件移动到公共目录下
     *
     * @param context Context
     * @param request RenameToFileRequest
     * @return FileResponse
     */
    private static boolean privateToPublic(Context context, CopyFileRequest request) {
        long time = System.currentTimeMillis();
        File srcFile = request.getSrcFile();
        File targetFile = request.getTargetFile();

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

            // step 2   create target file
            NewFileRequest createRequest = new NewFileRequest.Builder()
                    .setFile(targetFile)
                    .setContentValues(request.getContentValues())
                    .setImage(request.isImage())
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
                   return true;
                }
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(fileInputStream, fileOutputStream);
        }
        return false;

    }

    /**
     * 公共目录下的文件复制 到私有目录下
     *
     * @param context Context
     * @return boolean
     */
    private static boolean publicToPrivate(Context context, CopyFileRequest copyFileRequest) {
        long time = System.currentTimeMillis();
        File srcFile = copyFileRequest.getSrcFile();
        File targetFile = copyFileRequest.getTargetFile();

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
                mkdirResult = parentFile.mkdirs();
            }
            createNewFile = targetFile.createNewFile();
            // step 2
            OpenFileRequest openRequest = new OpenFileRequest.Builder()
                    .setModeType(FileConstants.FileModeType.MODE_READ)
                    .setFile(srcFile)
                    .setImage(copyFileRequest.isImage())
                    .setMediaId(copyFileRequest.getMediaId())
                    .setUri(copyFileRequest.getUri())
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
                    return true;
                }
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(fileInputStream, outputStream);
        }
        return false;
    }


    /**
     * 公共目录下的文件移动到 公共目录
     *
     * @param context         Context
     * @param copyFileRequest CopyFileRequest
     * @return boolean
     */
    private static boolean publicToPublic(Context context,
                                          CopyFileRequest copyFileRequest) {
        long time = System.currentTimeMillis();

        File srcFile = copyFileRequest.getSrcFile();
        File targetFile = copyFileRequest.getTargetFile();

        if (targetFile.exists()) {
            //目标文件存在，则先删除
            DeleteFileRequest deleteRequest = new DeleteFileRequest.Builder()
                    .setFile(targetFile)
                    .setImage(copyFileRequest.isImage())
                    .setMediaId(copyFileRequest.getMediaId())
                    .setUri(copyFileRequest.getUri())
                    .builder();
            DeleteFileByMediaStoreHelper.delete(context, deleteRequest);
        }
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {

            //打开源文件
            Uri srcUri = MediaStoreUriHelper.getUri(context, copyFileRequest);
            OpenFileRequest srcOpenRequest = new OpenFileRequest.Builder()
                    .setModeType(FileConstants.FileModeType.MODE_READ)
                    .setFile(srcFile)
                    .setUri(srcUri)
                    .builder();

            fileInputStream = (FileInputStream) OpenFileByMediaStoreHelper
                    .getInputStream(context, srcOpenRequest);

            //构建创建目标目录的Request
            NewFileRequest createRequest = new NewFileRequest.Builder()
                    .setFile(targetFile)
                    .setImage(copyFileRequest.isImage())
                    .setContentValues(copyFileRequest.getContentValues())
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
                }
                return true;
            }
        } catch (Exception e) {
        } finally {
            IOUtils.closeQuietly(fileInputStream, fileOutputStream);
        }
        return false;

    }


    private static boolean isPublicToPublic(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePublicDir(srcRelative))
                && (OppoEnvironment.isRelativePublicDir(targetRelative));
    }

    private static boolean isPublicToPrivate(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePublicDir(srcRelative))
                && (OppoEnvironment.isRelativePrivateDir(targetRelative));
    }

    private static boolean isPrivateToPublic(String srcRelative, String targetRelative) {
        return (OppoEnvironment.isRelativePrivateDir(srcRelative))
                && (OppoEnvironment.isRelativePublicDir(targetRelative));
    }
}
