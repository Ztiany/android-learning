package com.ztiany.flatbuffersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztiany.flatbuffersample.comparison.ComparisonActivity;
import com.ztiany.flatbuffersample.usage.UsageActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void compare(View view) {
        startActivity(new Intent(this, ComparisonActivity.class));
    }

    public void usage(View view) {
        startActivity(new Intent(this, UsageActivity.class));
    }

}
