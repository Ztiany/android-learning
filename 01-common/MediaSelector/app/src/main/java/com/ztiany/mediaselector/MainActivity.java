package com.ztiany.mediaselector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SysMediaSelector mSysMediaSelector;
    private ImageView mIv;

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");//统一生成图片的名称格式

    private String createStorePath() {
        File file = new File(this.getExternalCacheDir() + File.separator + "images" + File.separator + formatDate(new Date()) + ".jpg");
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            Log.d("MainActivity", "mkdirs:" + mkdirs);
        }
        return file.getAbsolutePath();
    }

    private static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIv = (ImageView) findViewById(R.id.image);

        mSysMediaSelector = new SysMediaSelector(ContextWrapper.create(this), new SysMediaSelector.MediaSelectorCallback() {
            @Override
            public void onTakeSuccess(String path) {
                if (new File(path).exists()) {
                    if (ifImage(path)) {
                        mIv.setImageBitmap(BitmapFactory.decodeFile(path));
                    } else {
                        Toast.makeText(MainActivity.this, path, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onTakeFail() {
                mIv.setImageResource(R.mipmap.ic_launcher);
            }
        });
        //务必授予权限，否则自己玩
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
    }

    private boolean ifImage(String path) {
        return path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".gif");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            supportFinishAfterTransition();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 获取图片
    ///////////////////////////////////////////////////////////////////////////

    public void takePhotoFormCamera(View view) {
        String storePath = createStorePath();
        mSysMediaSelector.takePhotoFromCameraAndCrop(storePath, null, "裁个图");
    }

    public void takePhotoFormCameraNoCrop(View view) {
        String storePath = createStorePath();
        mSysMediaSelector.takePhotoFromCamera(storePath);
    }

    public void takePhotoFromAlbum(View view) {
        mSysMediaSelector.takePhotoFormAlbumAndCrop(createStorePath(), null, "裁个图");
    }

    public void takePhotoFromAlbumNoCrop(View view) {
        mSysMediaSelector.takePhotoFormAlbum();
    }

    public void tokeFile(View view) {
        mSysMediaSelector.takeFile();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSysMediaSelector.onActivityResult(requestCode, resultCode, data);
    }
}
