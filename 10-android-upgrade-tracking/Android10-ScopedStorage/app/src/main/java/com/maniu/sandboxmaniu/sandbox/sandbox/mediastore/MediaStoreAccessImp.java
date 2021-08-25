/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : MediaStoreAccessImp.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          MediaStoreAccessImp
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.Context;
import android.os.ParcelFileDescriptor;


import com.maniu.sandboxmaniu.sandbox.sandbox.SAF.SAFAccessImp;
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

public class MediaStoreAccessImp implements FileAccessInterface {
    private static MediaStoreAccessImp sInstance;

    private MediaStoreAccessImp() {

    }

    public static MediaStoreAccessImp getInstance() {
        if (sInstance == null) {
            synchronized (SAFAccessImp.class) {
                if (sInstance == null) {
                    sInstance = new MediaStoreAccessImp();
                }
            }
        }
        return sInstance;
    }

    @Override
    public ParcelFileDescriptor openFile(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        return OpenFileByMediaStoreHelper.openFile(context, request);
    }


    @Override
    public FileResponse newCreateFile(Context context, NewFileRequest request) {
        return CreateFileByMediaStoreHelper.newCreateFile(context, request);
    }

    @Override
    public boolean delete(Context context, DeleteFileRequest request) {
        return DeleteFileByMediaStoreHelper.delete(context, request);
    }


    @Override
    public FileResponse renameTo(Context context, RenameToFileRequest renameRequest) {
        return MoveFileByMediaStoreHelper.renameTo(context, renameRequest);
    }

    @Override
    public boolean mkdirs(Context context, File file) {
        return CreateFileByMediaStoreHelper.mkdirs(context, file);
    }

    @Override
    public InputStream getInputStream(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        return OpenFileByMediaStoreHelper.getInputStream(context, request);
    }

    @Override
    public OutputStream getOutStream(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        return OpenFileByMediaStoreHelper.getOutStream(context, request);

    }

    @Override
    public boolean copyFile(Context context, CopyFileRequest copyFileRequest) {
        return CopyFileByMediaStoreHelper.copyFile(context, copyFileRequest);
    }


}
