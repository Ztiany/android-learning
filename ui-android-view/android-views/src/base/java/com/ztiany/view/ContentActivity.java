package com.ztiany.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-05 15:27
 */
public class ContentActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public static Intent getLaunchIntent(Context context, String title, Class fragment) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("key1", title);
        intent.putExtra("key2", fragment.getName());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_content_toolbar);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setContentInsetStartWithNavigation(0);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());
        }

        if (savedInstanceState == null) {
            String name = getIntent().getStringExtra("key2");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, Fragment.instantiate(this, name, null), name)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String title = getIntent().getStringExtra("key1");
        mToolbar.setTitle(title);
    }

}