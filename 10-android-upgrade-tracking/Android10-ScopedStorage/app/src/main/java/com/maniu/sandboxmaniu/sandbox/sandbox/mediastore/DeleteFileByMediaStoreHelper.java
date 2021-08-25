
/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : DeleteFileByMediaStoreHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          DeleteFileByMediaStoreHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.maniu.sandboxmaniu.sandbox.sandbox.request.DeleteFileRequest;


public class DeleteFileByMediaStoreHelper {
    private static final String TAG = "DeleteFileByMediaStoreHelper";
    private final static int RESULT_SUCCESS = 1;

    public static boolean delete(Context context, DeleteFileRequest request) {
        if (request == null) {
            return false;
        }
        long time = System.currentTimeMillis();

        Uri uri = MediaStoreUriHelper.getUri(context, request);
        ContentResolver resolver = context.getContentResolver();
        if (uri != null) {
            try {
                int result = resolver.delete(uri, null, null);
                return result == RESULT_SUCCESS;
            } catch (RecoverableSecurityException exception) {
                MediaStorePermissionHelper.startIntentSenderForResult(context, exception, MediaStorePermissionHelper.REQUEST_CODE_DELETE);
                return false;
            }
        }else {
        }
        return false;
    }

}
