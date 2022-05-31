package com.maniu.sandboxmaniu;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.maniu.sandboxmaniu.sandbox.sandbox.FileAccessManager;
import com.maniu.sandboxmaniu.sandbox.sandbox.request.NewFileRequest;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission( this);
    }
    public static boolean checkPermission(
            Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    public void createSAF(View view) {
        NewFileRequest newFileRequest = new NewFileRequest(new File(Environment.DIRECTORY_DOWNLOADS + "/Maniu"));
        FileAccessManager.getInstance().newCreateFile(this, newFileRequest);
    }

}