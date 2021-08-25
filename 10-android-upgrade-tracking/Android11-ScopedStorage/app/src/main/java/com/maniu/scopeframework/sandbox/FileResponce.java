package com.maniu.scopeframework.sandbox;

import android.net.Uri;

import java.io.File;

public class FileResponce {
    private boolean isSuccess;
//    路径  ---》 uri  ---》 读写
    private Uri uri;
    private File file;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
