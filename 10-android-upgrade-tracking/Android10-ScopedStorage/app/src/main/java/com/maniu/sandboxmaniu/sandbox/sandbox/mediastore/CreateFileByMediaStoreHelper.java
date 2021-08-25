/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : CreateFileByMediaStoreHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          CreateFileByMediaStoreHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.maniu.sandboxmaniu.sandbox.sandbox.constants.FileConstants;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;
import com.maniu.sandboxmaniu.sandbox.sandbox.response.FileResponse;

import java.io.File;


public class CreateFileByMediaStoreHelper {
    private static final String TAG = "CreateFileByMediaStoreHelper";

    public static boolean mkdirs(Context context, File file) {
        if (file == null) {
            return false;
        }
        long time = System.currentTimeMillis();

        Uri uri = MediaStoreUriHelper.getInsertContentUri(file.getAbsolutePath(), true);
        ContentValues contentValues = ContentValuesHelper.createMakeDirsContentValues(file);
        Uri result = null;
        if (uri != null) {
            ContentResolver resolver = context.getContentResolver();
            result = resolver.insert(uri, contentValues);
        }

        return result != null;

    }

    public static FileResponse newCreateFile(Context context, NewFileRequest request) {
        if (request != null) {
            long time = System.currentTimeMillis();
            Uri uri = MediaStoreUriHelper.getInsertContentUri(request);
            FileResponse response = newCreateFile(context, uri, request);
            return response;
        } else {
            return null;
        }

    }

    private static FileResponse newCreateFile(Context context, Uri uri, NewFileRequest request) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues contentValues = ContentValuesHelper.getContentValues(request);
        Uri resultUri = null;
        boolean result = false;
        if (uri != null) {
            resultUri = resolver.insert(uri, contentValues);
            result = resultUri != null;
        }
        return new FileResponse.Builder()
                .setUri(resultUri)
                .setAccessType(FileConstants.FileType.TYPE_MEDIA_STORE)
                .setSuccess(result)
                .builder();
    }


}
