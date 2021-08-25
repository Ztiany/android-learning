package com.ztiany.androidipc.activity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztiany.androidipc.R;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-17 16:12
 */
public class ProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
    }

    public void insert(View view) {
        Uri uri = Uri.parse("content://com.ztiany.androidipc.provider");
        ContentValues values = new ContentValues();
        values.put("key", "value from ProviderActivity");
        getContentResolver().insert(uri, values);
    }

}
