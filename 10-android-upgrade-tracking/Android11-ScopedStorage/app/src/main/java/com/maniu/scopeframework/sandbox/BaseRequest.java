package com.maniu.scopeframework.sandbox;

import java.io.File;

public class BaseRequest {
//    路径   相对
    private File file;

    public BaseRequest(File file) {
        this.file = file;
    }

    private String type;

    public File getFile() {

        return file;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
