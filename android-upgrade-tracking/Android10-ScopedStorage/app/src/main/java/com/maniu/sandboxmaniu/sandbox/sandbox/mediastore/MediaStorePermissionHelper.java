package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.app.Activity;
import android.app.RecoverableSecurityException;
import android.content.Context;
import android.content.IntentSender;


/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : MediaStorePermissionHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          MediaStorePermissionHelper
 ***********************************************************************/
public class MediaStorePermissionHelper {
    private static final String TAG = "MediaStorePermissionHelper";
    public final static int REQUEST_CODE_DELETE = 1001;
    public final static int REQUEST_CODE_UPDATE = 1002;


    public static void startIntentSenderForResult(Context context,
                                                  final RecoverableSecurityException exception,
                                                  final int requestCode) {

        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            IntentSender intentSender = exception.getUserAction()
                    .getActionIntent()
                    .getIntentSender();
            try {
                activity.startIntentSenderForResult(intentSender,
                        requestCode,
                        null,
                        0,
                        0,
                        0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }

        }
    }


}
