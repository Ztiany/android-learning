/*********************************************************************
 ** Copyright (C), 2010-2020, OPPO Mobile Comm Corp., Ltd.
 ** VENDOR_EDIT
 ** File        :
 ** Description : ContentValuesHelper.
 ** Version     : V1.0
 ** Date        : 2020-03-10
 ** Author      : dingyong@Apps.Gallery3D
 **
 ** ---------------------Revision History: ----------------------------
 **  <author>                    <date>          <version>      <desc>
 **  dingyong@Apps.Gallery3D     2020-03-10       V1.0          ContentValuesHelper
 ***********************************************************************/
package com.maniu.sandboxmaniu.sandbox.sandbox.mediastore;

import android.content.ContentValues;
import android.provider.MediaStore;

import com.maniu.sandboxmaniu.sandbox.sandbox.OppoEnvironment;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;

import java.io.File;

public class ContentValuesHelper {
    private final static String MIME_TYPE_IMAGE = "image/*";
    private final static String MIME_TYPE_VIDEO = "video/*";

    public static ContentValues createVideoContentValues(File file) {
        long savingTimestamp = System.currentTimeMillis();
        long now = savingTimestamp / 1000;
        String path = file.getAbsolutePath();
        String relativePath = OppoEnvironment.getRelativePath(path);
        String displayName = OppoEnvironment.getDisplayName(path);
        String title = OppoEnvironment.getFileName(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, title);
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_VIDEO);
        values.put(MediaStore.MediaColumns.DATE_TAKEN, savingTimestamp);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, now);
        values.put(MediaStore.MediaColumns.DATE_ADDED, now);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        // TODO
        return values;
    }

    public static ContentValues getContentValues(NewFileRequest request) {
        ContentValues contentValues = request.getContentValues();
        File file = request.getFile();
        if (contentValues == null) {
            Boolean aBoolean = request.isImage();
            boolean isImage = false;
            if (aBoolean != null) {
                isImage = aBoolean;
            }else {
                isImage = !MediaStoreUriHelper.isVideo(file.getAbsolutePath());
            }
            if (isImage) {
                contentValues = ContentValuesHelper.createImageContentValues(file);
            } else {
                contentValues = ContentValuesHelper.createVideoContentValues(file);
            }
        }
        return contentValues;
    }

    public static ContentValues createImageContentValues(File file) {
        long savingTimestamp = System.currentTimeMillis();
        long now = savingTimestamp / 1000;
        String path = file.getAbsolutePath();
        String relativePath = OppoEnvironment.getRelativePath(path);
        String displayName = OppoEnvironment.getDisplayName(path);
        String title = OppoEnvironment.getFileName(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.TITLE, title);
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE_IMAGE);
        values.put(MediaStore.MediaColumns.DATE_TAKEN, savingTimestamp);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, now);
        values.put(MediaStore.MediaColumns.DATE_ADDED, now);
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
        values.put(MediaStore.MediaColumns.SIZE, file.length());
        // TODO

        return values;
    }


    public static ContentValues createMakeDirsContentValues(File file) {
        String path = file.getAbsolutePath();
        String relativePath = OppoEnvironment.getRelativePath(path);
        String displayName = OppoEnvironment.getDisplayName(path);
        String title = OppoEnvironment.getFileName(path);
        ContentValues contentValues = new ContentValues();
        if (relativePath != null) {
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
        }
        if (displayName != null) {
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        }
        if (title != null) {
            contentValues.put(MediaStore.MediaColumns.TITLE, title);
        }
        return contentValues;
    }


}
