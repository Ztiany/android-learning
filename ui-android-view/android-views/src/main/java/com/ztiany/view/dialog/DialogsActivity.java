package com.ztiany.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ztiany.view.R;

import androidx.appcompat.app.AppCompatActivity;

public class DialogsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogs_activity_main);
    }

    public void openDialogActivity(View view) {
        startActivity(new Intent(this, DialogActivity.class));
    }

}
