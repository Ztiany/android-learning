package com.smartdengg.clickdebounce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button1).setOnClickListener(this);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick : " + this.getClass().getName());
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickProxy.getInstance().onClick(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!DebouncedPredictor.shouldDoClick(v)) {
            return;
        }
        startActivity(new Intent(MainActivity.this, SecondActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int a = 19999;
        Log.d(TAG, "onClick : " + a);
    }

}
