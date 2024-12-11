package me.ztiany.glideanalysis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void clearMemory(View view) {
        Glide.get(this).clearMemory();
    }

    public void clearDisk(View view) {
        mExecutor.execute(() -> {
            Glide.get(this).clearDiskCache();
            runOnUiThread(() -> {
                Toast.makeText(MainActivity.this, "completed", Toast.LENGTH_SHORT).show();
            });
        });
    }

    public void loadFromString(View view) {
        ImageView iv = findViewById(R.id.iv);
        Glide.with(this).load(
                "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1343238872,2009395483&fm=26&gp=0.jpg"
        ).into(iv);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExecutor.shutdown();
    }

}