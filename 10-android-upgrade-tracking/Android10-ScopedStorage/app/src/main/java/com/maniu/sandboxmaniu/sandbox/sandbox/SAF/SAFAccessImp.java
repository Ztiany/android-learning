/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : MediaStoreSandboxManager.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          SAFAccessImp
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.SAF;

import android.content.Context;
import android.os.ParcelFileDescriptor;


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

public class SAFAccessImp implements FileAccessInterface {
    private static final String TAG = "SAFAccessImp";
    private static SAFAccessImp sInstance;

    private SAFAccessImp() {

    }

    public static SAFAccessImp getInstance() {
        if (sInstance == null) {
            synchronized (SAFAccessImp.class) {
                if (sInstance == null) {
                    sInstance = new SAFAccessImp();
                }
            }
        }
        return sInstance;
    }


    @Override
    public ParcelFileDescriptor openFile(Context context, OpenFileRequest request) throws FileNotFoundException {
        return null;
    }

    @Override
    public FileResponse newCreateFile(Context context, NewFileRequest request) {
        return null;
    }

    @Override
    public boolean delete(Context context, DeleteFileRequest request) {
        return false;
    }

    @Override
    public FileResponse renameTo(Context context, RenameToFileRequest renameRequest) {
        return null;
    }

    @Override
    public boolean mkdirs(Context context, File file) {
        return false;
    }

    @Override
    public InputStream getInputStream(Context context, OpenFileRequest request) throws FileNotFoundException {
        return null;
    }

    @Override
    public OutputStream getOutStream(Context context, OpenFileRequest request) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean copyFile(Context context, CopyFileRequest copyFileRequest) {
        return false;
    }


}
