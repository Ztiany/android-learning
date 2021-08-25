package com.maniu.scopeframework.sandbox;

import android.os.Environment;
import android.provider.MediaStore;

import com.maniu.scopeframework.annotion.DbField;

import java.io.File;

public class FileRequest extends BaseRequest {
    @DbField(MediaStore.Downloads.DISPLAY_NAME)
    private String displayName;
    @DbField(MediaStore.Downloads.RELATIVE_PATH)
    private String path;//getPath   p-->P    getPath
    @DbField(MediaStore.Downloads.TITLE)
    private String title;

    public FileRequest(File file) {
        super(file);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return Environment.DIRECTORY_DOWNLOADS + "/"+path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
