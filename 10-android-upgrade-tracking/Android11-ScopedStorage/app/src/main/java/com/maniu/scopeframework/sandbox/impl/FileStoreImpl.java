package com.maniu.scopeframework.sandbox.impl;

import android.content.Context;

import com.maniu.scopeframework.sandbox.BaseRequest;
import com.maniu.scopeframework.sandbox.FileResponce;
import com.maniu.scopeframework.sandbox.IFile;

public class FileStoreImpl implements IFile {

    @Override
    public <T extends BaseRequest> FileResponce newCreateFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce delete(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce renameTo(Context context, T where, T request) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce copyFile(Context context, T baseRequest) {
        return null;
    }

    @Override
    public <T extends BaseRequest> FileResponce query(Context context, T baseRequest) {
        return null;
    }
}
