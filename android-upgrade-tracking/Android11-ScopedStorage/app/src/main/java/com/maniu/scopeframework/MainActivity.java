package com.maniu.scopeframework;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.maniu.scopeframework.sandbox.FileAccessFactory;
import com.maniu.scopeframework.sandbox.FileRequest;
import com.maniu.scopeframework.sandbox.FileResponce;
import com.maniu.scopeframework.sandbox.ImageRequest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission(this);
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
    public void create(View view) {
        FileRequest request = new FileRequest(new File("David12"));
        request.setPath(" David12");
        request.setDisplayName("David12");
        request.setTitle("David12");
        FileAccessFactory.getIFile(request).newCreateFile(this, request);
    }

    public void createImg(View view) {
        ImageRequest imageRequest = new ImageRequest(new File("Maniu.jpg"));
        imageRequest.setPath("Images");
        imageRequest.setDisplayName("修改后的图片.jpg");
        imageRequest.setMimeType("image/jpeg");
        FileResponce fileResponce=FileAccessFactory.getIFile(imageRequest).newCreateFile(this, imageRequest);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.die);
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(fileResponce.getUri());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "添加图片成功", Toast.LENGTH_SHORT).show();
    }

    public void queryFile(View view) {
//        前面是确定uri类型
        ImageRequest imageRequest = new ImageRequest(new File("修改后的图片.jpg"));
        imageRequest.setDisplayName("修改后的图片.jpg");
        FileAccessFactory.getIFile(imageRequest).query(this, imageRequest);
    }

    public void updateFile(View view) {
        ImageRequest where = new ImageRequest(new File("修改后的图片.jpg"));
        where.setDisplayName("修改后的图片.jpg");

        ImageRequest item = new ImageRequest(new File("david.jpg"));
        item.setDisplayName("David.jpg");
        FileAccessFactory.getIFile(where).renameTo(this, where, item);
    }

    public void deleteFile(View view) {
        ImageRequest where = new ImageRequest(new File("David.jpg"));
        where.setDisplayName("David.jpg");
        FileAccessFactory.getIFile(where).delete(this, where);
    }
}