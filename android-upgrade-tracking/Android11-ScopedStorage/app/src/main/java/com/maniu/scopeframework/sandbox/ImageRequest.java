package com.maniu.scopeframework.sandbox;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.maniu.scopeframework.annotion.DbField;

import java.io.File;

public class ImageRequest extends BaseRequest {
    public ImageRequest(File file) {
        super(file);
    }

    @DbField(MediaStore.Images.ImageColumns.MIME_TYPE)
    private String mimeType;
    @DbField(MediaStore.Downloads.DISPLAY_NAME)
    private String displayName;
    @DbField(MediaStore.Downloads.RELATIVE_PATH)
    private String path;//getPath   p-->P    getPath

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        if (!TextUtils.isEmpty(path)) {
            return Environment.DIRECTORY_PICTURES + "/" + path;
        }
        return null;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
