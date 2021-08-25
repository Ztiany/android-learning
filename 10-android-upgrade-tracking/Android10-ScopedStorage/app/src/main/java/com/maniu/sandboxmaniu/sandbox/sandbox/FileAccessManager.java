/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileAccessManager.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileAccessManager
 ***********************************************************************/

package com.maniu.sandboxmaniu.sandbox.sandbox;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess.FileAccessFactory;
import com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess.FileAccessInterface;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.CopyFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.RenameToFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.response.FileResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileAccessManager implements FileAccessInterface {
    private static final String TAG = "FileAccessManager";
    private static FileAccessManager sInstance;
    private static final String MODE_READ = "r";

    private FileAccessManager() {

    }

    public static FileAccessManager getInstance() {
        if (sInstance == null) {
            synchronized (FileAccessManager.class) {
                if (sInstance == null) {
                    sInstance = new FileAccessManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 通过uri 打开文件 返回 ParcelFileDescriptor
     *
     * @param context context
     * @param uri     媒体库文件的uri或者SAF的document uri
     * @return ParcelFileDescriptor
     * @throws FileNotFoundException
     */
    public ParcelFileDescriptor openFile(Context context, Uri uri) throws FileNotFoundException {
        if (uri == null) {
            return null;
        }
        return context.getContentResolver().openFileDescriptor(uri, FileConstants.FileMode.MODE_READ);
    }

    /**
     * 通过uri 打开文件 返回 ParcelFileDescriptor
     *
     * @param context context
     * @param uri     媒体库文件的uri或者SAF的document uri
     * @return ParcelFileDescriptor
     * @throws FileNotFoundException
     */
    public ParcelFileDescriptor openFileWrite(Context context, Uri uri) throws FileNotFoundException {
        if (uri == null) {
            return null;
        }
        return context.getContentResolver().openFileDescriptor(uri, FileConstants.FileMode.MODE_WRITE);
    }

    /**
     * openFile 在拿不到 uri的情况下 通过File去打开文件
     *
     * @param context context
     * @param request OpenFileRequest  请求参数
     * @return ParcelFileDescriptor
     */
    public ParcelFileDescriptor openFile(Context context, OpenFileRequest request) throws FileNotFoundException {
        if ((request == null) || (request.getFile() == null)) {
            return null;
        }
        return FileAccessFactory.getOpenFileAccess(request.getFilePath())
                .openFile(context, request);
    }

    /**
     * 获取文件的InputStream
     * 如果能拿到
     *
     * @param context context
     * @param request OpenFileRequest
     * @return InputStream
     * @throws FileNotFoundException
     */
    public InputStream getInputStream(Context context, OpenFileRequest request) throws FileNotFoundException {
        if ((request == null) || (request.getFile() == null)) {
            return null;
        }
        return FileAccessFactory.getOpenFileAccess(request.getFilePath())
                .getInputStream(context, request);
    }

    /**
     * 获取文件的utStream
     *
     * @param context Context
     * @param request OpenFileRequest
     * @return OutputStream
     * @throws FileNotFoundException
     */
    public OutputStream getOutStream(Context context, OpenFileRequest request) throws FileNotFoundException {
        if ((request == null) || (request.getFile() == null)) {
            return null;
        }
        return FileAccessFactory.getCreateFileAccess(request.getFilePath())
                .getOutStream(context, request);
    }


    /**
     * newCreateFile
     * 新建一个文件，
     *
     * @param context Context
     * @param request NewFileRequest 需要传入 File
     * @return FileResponse
     */
    public FileResponse newCreateFile(Context context, NewFileRequest request) {
        if ((request == null) || (request.getFile() == null)) {
            return null;
        }
        return FileAccessFactory.getCreateFileAccess(request.getFilePath())
                .newCreateFile(context, request);
    }

    /**
     * delete  删除文件
     *
     * @param context Context
     * @param request DeleteFileRequest
     * @return FileResponse
     */
    @Override
    public boolean delete(Context context, DeleteFileRequest request) {
        if ((request == null) || (request.getFile() == null)) {
            return false;
        }
        return FileAccessFactory.getDeleteFileAccess(request.getFilePath())
                .delete(context, request);
    }

    /**
     * srcFile renameTo targetFile
     *
     * @param context Context
     * @param request RenameToFileRequest
     * @return FileResponse
     */
    public FileResponse renameTo(Context context, RenameToFileRequest request) {
        if ((request == null) || (request.getSrcFile() == null) || (request.getTargetFile() == null)) {
            return null;
        }
        return FileAccessFactory.getMoveFileAccess(
                request.getSrcFilePath(),
                request.getTargetFilePath())
                .renameTo(context, request);
    }

    /**
     * mkdirs  创建目录
     *
     * @param context Context
     * @param file    File
     * @return FileResponse
     */
    public boolean mkdirs(Context context, File file) {
        if (file == null) {
            return false;
        }
        return FileAccessFactory.getCreateFileAccess(file.getAbsolutePath())
                .mkdirs(context, file);
    }

    /**
     * copy file
     *
     * @param context         Context
     * @param copyFileRequest CopyFileRequest
     * @return boolean
     */
    @Override
    public boolean copyFile(Context context, CopyFileRequest copyFileRequest) {
        if ((copyFileRequest == null) || (copyFileRequest.getSrcFile() == null) || (copyFileRequest.getSrcFilePath() == null)) {
            return false;
        }
        return FileAccessFactory.getCopyFileAccess(copyFileRequest.getSrcFilePath(), copyFileRequest.getTargetFilePath())
                .copyFile(context, copyFileRequest);
    }

}
