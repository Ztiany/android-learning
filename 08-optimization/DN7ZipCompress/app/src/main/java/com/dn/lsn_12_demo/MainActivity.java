package com.dn.lsn_12_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        int result = ZipCode.exec("7zr a " + out.getAbsolutePath() + " " + src.getAbsolutePath() + " -mx=9");
        Log.e(TAG, "ZipCode.exec: "+result);
    }

    /**
     * 将assets下的可执行文件拷贝到应用私有目录
     */
    public void load(View view) {
        boolean result = ZipHelper.loadBinary(this, "7zr");
        Toast.makeText(this, "加载7zr结果：" + result, Toast.LENGTH_SHORT).show();
    }

    /**
     * 压缩
     * 7zr a  [输出文件] [待压缩文件/目录] -mx=9
     * 7zr a /sdcard/7-Zip.7z /sdcard/7-Zip -mx=9
     */
    public void pack(View view) {
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        ZipHelper.execute(this, "7zr a " + out.getAbsolutePath() + " "
                + src.getAbsolutePath() + " -mx=9", new ZipHelper.OnResultListener() {
            @Override
            public void onSuccess(String msg) {
                Log.e(TAG, "执行成功");
            }

            @Override
            public void onFailure(int errorno, String msg) {
                Log.e(TAG, "执行失败");
                Log.e(TAG, "错误码："+errorno);
                Log.e(TAG, "错误信息："+msg);
            }

            @Override
            public void onProgress(String msg) {
                Log.e(TAG, "正在执行:" + msg);
            }
        });
    }

    /**
     * 解压
     * 7zr x [压缩文件]  -o[输出目录]
     *
     */
    public void unpack(View view) {
        File src = new File(Environment.getExternalStorageDirectory(), "7-Zip.7z");
        File out = new File(Environment.getExternalStorageDirectory(), "7-Zip-unpack");
        ZipHelper.execute(this, "7zr x " + src.getAbsolutePath() + " -o"
                + out.getAbsolutePath(), new ZipHelper.OnResultListener() {
            @Override
            public void onSuccess(String msg) {
                Log.e(TAG, "执行成功");
            }

            @Override
            public void onFailure(int errorno, String msg) {
                Log.e(TAG, "执行失败");
                Log.e(TAG, "错误码："+errorno);
                Log.e(TAG, "错误信息："+msg);
            }

            @Override
            public void onProgress(String msg) {
                Log.e(TAG, "正在执行:" + msg);
            }
        });
    }

}