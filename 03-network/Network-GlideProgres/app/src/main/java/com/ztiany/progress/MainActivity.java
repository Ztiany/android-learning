package com.ztiany.progress;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private ImageListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clear(View view) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void[] params) {
                Glide.get(MainActivity.this).clearDiskCache();
                return null;
            }
        }.execute();
    }

    public void add(View view) {
        mFragment = new ImageListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, mFragment)
                .commit();
    }

    public void remove(View view) {
        if (mFragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mFragment)
                    .commit();
        }
    }

}
