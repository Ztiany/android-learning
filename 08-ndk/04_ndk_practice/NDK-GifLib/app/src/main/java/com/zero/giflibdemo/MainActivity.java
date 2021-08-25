package com.zero.giflibdemo;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import com.zero.giflibdemo.gif.GifDrawable;
import com.zero.giflibdemo.gif.GifFrame;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        }

        mImageView = findViewById(R.id.image);

        //BitmapDrawable
        GifDrawable gifDrawable;

        try {
            gifDrawable = new GifDrawable(GifFrame.decodeStream(getAssets().open("fire.gif")));
            mImageView.setImageDrawable(gifDrawable);
            gifDrawable.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}