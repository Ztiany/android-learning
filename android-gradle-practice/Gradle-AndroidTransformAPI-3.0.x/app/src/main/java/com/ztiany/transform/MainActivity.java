package com.ztiany.transform;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new ClassA();
        new ClassB();

        findViewById(R.id.click1).setOnClickListener(v -> Toast.makeText(this, "click1", Toast.LENGTH_SHORT).show());
        findViewById(R.id.click2).setOnClickListener(v -> Toast.makeText(this, "click2", Toast.LENGTH_SHORT).show());
        findViewById(R.id.click3).setOnClickListener(v -> Toast.makeText(this, "click3", Toast.LENGTH_SHORT).show());
    }

}
