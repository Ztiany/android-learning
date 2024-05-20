package com.ztiany.bsdiffapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ztiany.bsdiffapp.MainActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-02-08 15:19
 */
public class AppUpdater {

    private static final String TAG = AppUpdater.class.getSimpleName();

    private Context mContext;

    private static final String URL = "http://192.168.0.110:8080/LoginforAndroid/new.patch";

    private PatchUtils mPatchUtils;

    private final String mPatchFilePath;
    private final String mNewVersionPath;

    public AppUpdater(MainActivity mainActivity) {
        mContext = mainActivity.getApplicationContext();
        File externalCacheDir = mContext.getExternalCacheDir();
        mPatchFilePath = new File(externalCacheDir, "patch").getAbsolutePath();
        Log.d(TAG, mPatchFilePath);
        mNewVersionPath = new File(externalCacheDir, "new_version.apk").getAbsolutePath();
        mPatchUtils = new PatchUtils();
    }

    public void doUpdate() {
        new DownloadAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private class DownloadAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            //模拟下载
            if (!downloadPatch()) {
                Log.d(TAG, "已是最新版本");
                return false;
            }
            String sourceApkPath = ApkUtils.getSourceApkPath(mContext, mContext.getPackageName());
            if (TextUtils.isEmpty(sourceApkPath)) {
                Log.d(TAG, "获取安装包路径失败");
                return false;
            }
            try {
                mPatchUtils.mergeFile(sourceApkPath, mNewVersionPath, mPatchFilePath);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return false;
            }
            Log.d(TAG, "合并成功");
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(mContext, "检测到新版本，进行免流量更新", Toast.LENGTH_SHORT).show();
                ApkUtils.installApk(mContext, mNewVersionPath);
            } else {
                Toast.makeText(mContext, "已是最新版本", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aBoolean);
        }
    }

    private boolean downloadPatch() {

        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(URL);
            is = url.openStream();
            FileOutputStream fos = new FileOutputStream(mPatchFilePath);
            bos = new BufferedOutputStream(fos);

            byte[] buff = new byte[1024 * 4];
            int length;

            while ((length = is.read(buff)) != -1) {
                bos.write(buff, 0, length);
            }
            bos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}
