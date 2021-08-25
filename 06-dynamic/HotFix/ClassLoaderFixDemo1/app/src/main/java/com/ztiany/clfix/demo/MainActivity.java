package com.ztiany.clfix.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fix(View view) {

        //目录：/data/data/packageName/odex
        File fileDir = getDir(Constants.DEX_DIR, Context.MODE_PRIVATE);
        //往该目录下面放置我们修复好的dex文件。
        String name = "classes2.dex";
        String filePath = fileDir.getAbsolutePath() + File.separator + name;
        File file = new File(filePath);
        if (file.exists()) {
            boolean delete = file.delete();
            Log.d(TAG, "delete:" + delete);
        }

        //把下载好的在SD卡里面的修复了的classes2.dex搬到应用目录filePath
        InputStream is = null;
        FileOutputStream os = null;
        try {
            is = getAssets().open(name);
            os = new FileOutputStream(filePath);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            File f = new File(filePath);
            if (f.exists()) {
                Toast.makeText(this, "dex 重写成功", Toast.LENGTH_SHORT).show();
            }

            //热修复
            FixDexUtils.loadFixedDex(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(is);
            close(os);
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testBug(View view) {
        try {
            new Bug().testFix(this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
