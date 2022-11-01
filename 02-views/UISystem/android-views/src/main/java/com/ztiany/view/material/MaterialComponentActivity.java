package com.ztiany.view.material;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztiany.view.Item;
import com.ztiany.view.ItemAdapter;
import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

public class MaterialComponentActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final List<Item> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> supportFinishAfterTransition());
        }

        mRecyclerView = findViewById(R.id.activity_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerView.setAdapter(new ItemAdapter(this, LIST));
    }

    static {
        LIST.add(new Item("ShapeableImageView", ShapeableImageViewFragment.class));
        LIST.add(new Item("MaterialButton", MaterialButtonFragment.class));
        LIST.add(new Item("CustomShapeLayout", MaterialShapeDrawableFragment.class));
    }

}
