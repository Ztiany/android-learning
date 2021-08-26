package com.maniu.scopeframework.sandbox;

import android.os.Environment;

import com.maniu.scopeframework.sandbox.impl.FileStoreImpl;
import com.maniu.scopeframework.sandbox.impl.MediaStoreAccessImp;

public class FileAccessFactory {
    public static IFile getIFile(BaseRequest baseRequest) {
//        true  File  执行
        if (!Environment.isExternalStorageLegacy()) {
//Android 11版本
            setFileType(baseRequest);
            return MediaStoreAccessImp.getInstance();
        }else {
            return new FileStoreImpl();
        }
    }

    private static void setFileType(BaseRequest request) {
        if (request.getFile().getAbsolutePath().endsWith(".mp3") ||
                request.getFile().getAbsolutePath().endsWith(".wav")) {
            request.setType(MediaStoreAccessImp.AUDIO);
        }else if(request.getFile().getAbsolutePath().startsWith(MediaStoreAccessImp.VIDEO)
                ||request.getFile().getAbsolutePath().endsWith(".mp4")
                || request.getFile().getAbsolutePath().endsWith(".rmvb")
                ||request.getFile().getAbsolutePath().endsWith(".avi")){
            request.setType(MediaStoreAccessImp.VIDEO);
        }else if(request.getFile().getAbsolutePath().startsWith(MediaStoreAccessImp.IMAGE)
                || request.getFile().getAbsolutePath().endsWith(".jpg")
                ||request.getFile().getAbsolutePath().endsWith(".png")){
            request.setType(MediaStoreAccessImp.IMAGE);
        }else {
            request.setType(MediaStoreAccessImp.DOWNLOADS);
        }
    }
}
