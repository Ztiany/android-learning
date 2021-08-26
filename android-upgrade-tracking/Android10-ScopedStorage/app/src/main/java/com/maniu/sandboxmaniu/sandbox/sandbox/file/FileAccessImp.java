/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : FileAccessImp.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          FileAccessImp
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.file;

import android.content.Context;
import android.os.ParcelFileDescriptor;


import com.maniu.sandboxmaniu.sandbox.sandbox.IOUtils;
import com.maniu.sandboxmaniu.sandbox.sandbox.SAF.SAFAccessImp;
import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.fileaccess.FileAccessInterface;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.CopyFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.RenameToFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.response.FileResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileAccessImp implements FileAccessInterface {
    private static final String TAG = "FileAccessImp";
    private static FileAccessImp sInstance;

    private FileAccessImp() {

    }

    public static FileAccessImp getInstance() {
        if (sInstance == null) {
            synchronized (SAFAccessImp.class) {
                if (sInstance == null) {
                    sInstance = new FileAccessImp();
                }
            }
        }
        return sInstance;
    }


    @Override
    public ParcelFileDescriptor openFile(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        if (request == null || request   == null) {
            return null;
        }
        return ParcelFileDescriptor.open(request.getFile()    ,
                request.getOpenFdMode());

    }


    @Override
    public FileResponse newCreateFile(Context context, NewFileRequest request) {
        if ((request == null) || (request   == null)) {
            return null;
        }
        File file = request.getFile()  ;
        boolean result = false;
        try {
            File parnetFile = file.getParentFile();
            if (!parnetFile.exists()) {
                parnetFile.mkdirs();
            }
            result = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FileResponse
                .Builder()
                .setSuccess(result)
                .setAccessType(FileConstants.FileType.TYPE_FILE)
                .builder();

    }

    @Override
    public boolean delete(Context context, DeleteFileRequest request) {
        if ((request == null) || (request   == null)) {
            return false;
        }
        File file = request.getFile()  ;
        boolean result = false;
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }


    @Override
    public FileResponse renameTo(Context context,
                                 RenameToFileRequest renameRequest) {
        if ((renameRequest == null)
                || (renameRequest.getSrcFile() == null)
                || (renameRequest.getTargetFile() == null)) {
            return null;
        }
        boolean result = renameRequest.getSrcFile().renameTo(renameRequest.getTargetFile());
        return new FileResponse.Builder()
                .setAccessType(FileConstants.FileType.TYPE_FILE)
                .setSuccess(result).builder();
    }


    @Override
    public boolean mkdirs(Context context, File file) {
        if ((file == null)) {
            return false;
        }
        if (file.isDirectory()) {
            File parentFile = file.getParentFile();
            if (parentFile != null) {
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
            }
        }
        return file.mkdirs();
    }

    @Override
    public InputStream getInputStream(Context context, OpenFileRequest openRequest)
            throws FileNotFoundException {
        if (openRequest == null || openRequest   == null) {
            return null;
        }
        File file = openRequest.getFile()  ;
        return new FileInputStream(file );
    }

    @Override
    public OutputStream getOutStream(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        if ((request == null) || (request   == null)) {
            return null;
        }
        File file = request.getFile()  ;
        return new FileOutputStream(file  );
    }

    @Override
    public boolean copyFile(Context context, CopyFileRequest copyFileRequest) {
        if ((copyFileRequest == null) ||
                (copyFileRequest.getSrcFile() == null) ||
                (copyFileRequest.getTargetFile() == null)) {
            return false;
        }
        File srcFile = copyFileRequest.getSrcFile();
        File targetFile = copyFileRequest.getTargetFile();
        FileInputStream inputStreamSrc = null;
        FileOutputStream outputStreamTarget = null;
        try {
            inputStreamSrc = new FileInputStream(srcFile  );
            outputStreamTarget = new FileOutputStream(targetFile  );
            android.os.FileUtils.copy(inputStreamSrc, outputStreamTarget);
            return true;
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(inputStreamSrc, outputStreamTarget);
        }
        return false;
    }
}
