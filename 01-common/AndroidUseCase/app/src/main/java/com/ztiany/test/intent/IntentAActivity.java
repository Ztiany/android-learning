package com.ztiany.test.intent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.ztiany.test.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-16 16:19
 */
public class IntentAActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_a);
    }

    public void openIntentB(View view) {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.parse("ztiany://abs"), "text/paint");
        startActivityForResult(intent, 100);
    }

}