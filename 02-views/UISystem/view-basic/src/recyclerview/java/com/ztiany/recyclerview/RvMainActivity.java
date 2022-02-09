package com.ztiany.recyclerview;

import android.os.Bundle;

import com.ztiany.recyclerview.adapter_list.ListViewFragment;
import com.ztiany.recyclerview.adapter_list.RecyclerViewFragment;
import com.ztiany.recyclerview.diff_util.AsyncListDifferFragment;
import com.ztiany.recyclerview.diff_util.DiffUtilFragment;
import com.ztiany.recyclerview.item_decoraion_index.ItemDecorationIndexFragment;
import com.ztiany.recyclerview.itemtouch.GridFragment;
import com.ztiany.recyclerview.itemtouch.ItemTouchListenerFragment;
import com.ztiany.recyclerview.itemtouch.LinearFragment;
import com.ztiany.recyclerview.layout_manager.CustomLayoutManagerFragment;
import com.ztiany.recyclerview.layout_manager.SideCardLayoutManagerFragment;
import com.ztiany.recyclerview.snap.SnapHelperFragment;
import com.ztiany.recyclerview.swipe_menu.SwipeMenu1Fragment;
import com.ztiany.recyclerview.viewpager2.ViewPager2Fragment;
import com.ztiany.recyclerview.wrap_content.WithScrollViewFragment;
import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 技术研究
 */
public class RvMainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final List<Item> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_common_rv_activity_main);
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerView.setAdapter(new ItemAdapter(this, LIST));
    }

    static {
        LIST.add(new Item("Wrap RecyclerView", WithScrollViewFragment.class));
        LIST.add(new Item("ItemDecoration 实现分组索引", ItemDecorationIndexFragment.class));
        LIST.add(new Item("ItemTouchListener 研究", ItemTouchListenerFragment.class));
        LIST.add(new Item("ItemTouch Linear", LinearFragment.class));
        LIST.add(new Item("ItemTouch Grid", GridFragment.class));
        LIST.add(new Item("Pager Snap Helper", SnapHelperFragment.class));
        LIST.add(new Item("ScrollView 实现 SwipeMenu", SwipeMenu1Fragment.class));
        LIST.add(new Item("自定义 LinearLayoutManager", CustomLayoutManagerFragment.class));
        LIST.add(new Item("自定义卡片 LayoutManager", SideCardLayoutManagerFragment.class));
        LIST.add(new Item("DiffUtil 示例", DiffUtilFragment.class));
        LIST.add(new Item("AsyncListDiffer 示例", AsyncListDifferFragment.class));
        LIST.add(new Item("Adapter 对比 ListView", ListViewFragment.class));
        LIST.add(new Item("Adapter 对比 RecyclerView", RecyclerViewFragment.class));
        LIST.add(new Item("ViewPager 2", ViewPager2Fragment.class));
    }

}
