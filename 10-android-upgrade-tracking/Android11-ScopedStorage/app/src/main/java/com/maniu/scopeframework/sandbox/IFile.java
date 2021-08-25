package com.maniu.scopeframework.sandbox;

import android.content.Context;

public interface IFile {
    //    文件   需要被定义
    <T extends  BaseRequest> FileResponce newCreateFile(Context context, T baseRequest);
    <T extends  BaseRequest> FileResponce delete(Context context, T baseRequest);
    <T extends  BaseRequest> FileResponce renameTo(Context context, T where,T request);
    <T extends  BaseRequest> FileResponce copyFile(Context context, T baseRequest);
    <T extends  BaseRequest> FileResponce query(Context context, T baseRequest);
}
