
/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : OpenFileByMediaStoreHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          OpenFileByMediaStoreHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.OpenFileRequest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class OpenFileByMediaStoreHelper {
    private static final String TAG = "OpenFileByMediaStoreHelper";

    public static ParcelFileDescriptor openFile(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        if (request == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        Uri uri = MediaStoreUriHelper.getUri(context, request);
        ParcelFileDescriptor fileDescriptor = null;
        if (uri != null) {
            fileDescriptor = context.getContentResolver()
                    .openFileDescriptor(uri, request.getMode());
        }
        return fileDescriptor;
    }

    public static InputStream getInputStream(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        long time = System.currentTimeMillis();
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();

        if (uri == null) {
            uri = MediaStoreUriHelper.getUri(context, request);
            if (uri == null) {
                return null;
            }
        }
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return inputStream;
    }

    public static OutputStream getOutStream(Context context, OpenFileRequest request)
            throws FileNotFoundException {
        long time = System.currentTimeMillis();
        if (request == null) {
            return null;
        }
        Uri uri = request.getUri();
        if (uri == null) {
            uri = MediaStoreUriHelper.getUri(context, request);
            if (uri == null) {
                return null;
            }
        }

        OutputStream outputStream = context.getContentResolver().
                openOutputStream(uri, request.getMode());
        return outputStream;
    }
}
