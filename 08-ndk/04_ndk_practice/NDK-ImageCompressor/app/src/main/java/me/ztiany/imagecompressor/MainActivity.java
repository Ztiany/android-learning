package me.ztiany.imagecompressor;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    static {
        System.loadLibrary("native-lib");
    }

    private boolean isNative = false;

    private SystemMediaSelector mSystemMediaSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }

        mSystemMediaSelector = new SystemMediaSelector(this, new SystemMediaSelector.MediaSelectorCallback() {
            @Override
            public void onTakeSuccess(String path) {
                if (isNative) {
                    doNativeCompress(path);
                } else {
                    doSDKCompress(path);
                }
            }

            @Override
            public void onTakeFail() {

            }
        });
    }

    private void doSDKCompress(String path) {
        Log.d(TAG, "doSDKCompress---------------------");
        String savePath = createSavePath();
        try {
            BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(savePath));
            Log.d(TAG, "original---------------------");
            printFileInfo(path);
            Log.d(TAG, "compressed---------------------");
            printFileInfo(savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createSavePath() {
        return getExternalCacheDir().toString() + "/" + UUID.randomUUID() + ".jpeg";
    }

    private void doNativeCompress(String path) {
        Log.d(TAG, "doNativeCompress---------------------");
        String savePath = createSavePath();
        ImageCompressor.nativeCompress(BitmapFactory.decodeFile(path), 50, savePath);
        Log.d(TAG, "original---------------------");
        printFileInfo(path);
        Log.d(TAG, "compressed---------------------");
        printFileInfo(savePath);
    }

    private void printFileInfo(String savePath) {
        File file = new File(savePath);
        Log.d(TAG, file.toString());
        if (file.exists()) {
            Log.d(TAG, Formatter.formatFileSize(MainActivity.this, file.length()));
        }
    }

    public void doNative(View view) {
        isNative = true;
        mSystemMediaSelector.takePhotoFormAlbum();
    }

    public void doSDK(View view) {
        isNative = false;
        mSystemMediaSelector.takePhotoFormAlbum();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 1 && grantResults[0] != PERMISSION_GRANTED) {
            supportFinishAfterTransition();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSystemMediaSelector.onActivityResult(requestCode, resultCode, data);
    }

}