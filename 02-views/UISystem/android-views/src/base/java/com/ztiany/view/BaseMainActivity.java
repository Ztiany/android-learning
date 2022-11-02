package com.ztiany.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztiany.recyclerview.RecyclerViewActivity;
import com.ztiany.view.animation.circular_reveal.CircularRevealActivity;
import com.ztiany.view.animation.reversal.ReversalActivity;
import com.ztiany.view.animation.spring.SpringScrollViewFragment;
import com.ztiany.view.animation.square.SquareAnimationFragment;
import com.ztiany.view.bitmap.BitmapActivity;
import com.ztiany.view.constraint.ConstraintLayoutActivity;
import com.ztiany.view.courses.hencoderplus.HenCoderPlusFragment;
import com.ztiany.view.courses.hencoderplus.viewroot.ViewRootActivity;
import com.ztiany.view.custom.CustomViewFragment;
import com.ztiany.view.custom.flow_layout.FlowLayoutFragment;
import com.ztiany.view.custom.message_drag.MessageDragFragment;
import com.ztiany.view.custom.pull_refresh.PullToRefreshFragment;
import com.ztiany.view.custom.view_drag_helper.ViewDragHelperFragment;
import com.ztiany.view.custom.view_pager.ViewPagerFragment;
import com.ztiany.view.dialog.DialogsActivity;
import com.ztiany.view.draw.CanvasFragment;
import com.ztiany.view.draw.MatrixFragment;
import com.ztiany.view.draw.PathFragment;
import com.ztiany.view.draw.camera.Camera3DFragment;
import com.ztiany.view.draw.camera.Camera3DTheoryFragment;
import com.ztiany.view.draw.camera.CameraDemoViewFragment;
import com.ztiany.view.draw.color.ColorMatrixFilterFragment;
import com.ztiany.view.draw.color.MaskFilterFragment;
import com.ztiany.view.draw.overdraw.OverDrawFragment;
import com.ztiany.view.draw.text.SimpleTextGradualFragment;
import com.ztiany.view.draw.text.TextGradualViewPagerFragment;
import com.ztiany.view.draw.text.TextMeasureFragment;
import com.ztiany.view.drawable.DrawableBitmapFragment;
import com.ztiany.view.drawable.DrawableLayerFragment;
import com.ztiany.view.drawable.DrawableRotateFragment;
import com.ztiany.view.drawable.DrawableSelectorFragment;
import com.ztiany.view.drawable.DrawableVectorFragment;
import com.ztiany.view.drawable.FishDrawableFragment;
import com.ztiany.view.drawable.SVGChinaFragment;
import com.ztiany.view.inflater.LayoutInflaterActivity;
import com.ztiany.view.material.MaterialComponentActivity;
import com.ztiany.view.scroll.ScrollFragment;
import com.ztiany.view.scroll.sticky.StickyNavigationFragment;
import com.ztiany.view.window.RealWindowSizeActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMainActivity extends AppCompatActivity {

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
        RecyclerView recyclerView = findViewById(R.id.activity_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Item> items = new ArrayList<>();
        provideItems(items);
        recyclerView.setAdapter(new ItemAdapter(this, items));
    }

    protected abstract void provideItems(List<Item> items);

}