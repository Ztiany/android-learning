/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileAccessInterface.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileAccessInterface
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess;

import android.content.Context;
import android.os.ParcelFileDescriptor;

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

public interface FileAccessInterface {
    /**
     * @param context Context
     * @param request NewFileRequest
     * @return ParcelFileDescriptor
     * @throws FileNotFoundException FileNotFoundException
     */
    ParcelFileDescriptor openFile(Context context, OpenFileRequest request)
            throws FileNotFoundException;

    /**
     * newCreateFile()
     *
     * @param context Context
     * @param request context
     * @return FileResponse
     */
    FileResponse newCreateFile(Context context, NewFileRequest request);


    /**
     * delete file
     *
     * @param context context
     * @param request DeleteFileRequest
     * @return boolean
     */
    boolean delete(Context context, DeleteFileRequest request);

    /**
     * file.renameToe
     *
     * @param context       Context
     * @param renameRequest RenameToFileRequest
     * @return boolean
     */
    FileResponse renameTo(Context context, RenameToFileRequest renameRequest);

    /**
     * mkdirs
     *
     * @param context Context
     * @param file    File
     * @return FileResponse
     */

    boolean mkdirs(Context context, File file);


    /**
     * getInputStream
     *
     * @param context Context
     * @param request NewFileRequest
     * @return InputStream
     * @throws FileNotFoundException FileNotFoundException
     */
    InputStream getInputStream(Context context, OpenFileRequest request)
            throws FileNotFoundException;

    /**
     * getOutStream
     *
     * @param context Context
     * @param request NewFileRequest
     * @return InputStream
     * @throws FileNotFoundException FileNotFoundException
     */
    OutputStream getOutStream(Context context, OpenFileRequest request)
            throws FileNotFoundException;


    /**
     * copyFile
     *
     * @param context         Context
     * @param copyFileRequest CopyFileRequest
     * @return FileResponse
     */
    boolean copyFile(Context context, CopyFileRequest copyFileRequest);
}
