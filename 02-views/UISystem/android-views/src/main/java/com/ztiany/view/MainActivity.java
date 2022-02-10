package com.ztiany.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ztiany.recyclerview.RvMainActivity;
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
import com.ztiany.view.scroll.ScrollFragment;
import com.ztiany.view.scroll.sticky.StickyNavigationFragment;
import com.ztiany.view.window.RealWindowSizeActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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

        RecyclerView recyclerView = findViewById(R.id.activity_main);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new ItemAdapter(this, LIST));
    }

    static {
        LIST.add(new Item("RecyclerView 学习", RvMainActivity.class));

        LIST.add(new Item("HenCoderPlus 练习", HenCoderPlusFragment.class));
        LIST.add(new Item("HenCoderPlus ViewRoot", ViewRootActivity.class));

        LIST.add(new Item("Bitmap研究", BitmapActivity.class));
        LIST.add(new Item("事件 & 滑动", ScrollFragment.class));
        LIST.add(new Item("Sticky Navigation", StickyNavigationFragment.class));
        LIST.add(new Item("Canvas 绘制", CanvasFragment.class));
        LIST.add(new Item("矩阵", MatrixFragment.class));
        LIST.add(new Item("ColorMatrixFilter", ColorMatrixFilterFragment.class));
        LIST.add(new Item("MaskFilter", MaskFilterFragment.class));
        LIST.add(new Item("Path &贝塞尔曲线", PathFragment.class));
        LIST.add(new Item("Camera 变换", CameraDemoViewFragment.class));
        LIST.add(new Item("Camera3D View", Camera3DFragment.class));
        LIST.add(new Item("Camera3D 原理", Camera3DTheoryFragment.class));
        LIST.add(new Item("文字渐变", SimpleTextGradualFragment.class));
        LIST.add(new Item("文字测量", TextMeasureFragment.class));
        LIST.add(new Item("文字渐变 ViewPager", TextGradualViewPagerFragment.class));
        LIST.add(new Item("过度绘制", OverDrawFragment.class));

        LIST.add(new Item("ViewDragHelper使用", ViewDragHelperFragment.class));
        LIST.add(new Item("自定义 View", CustomViewFragment.class));
        LIST.add(new Item("消息的拖拽", MessageDragFragment.class));
        LIST.add(new Item("流式布局", FlowLayoutFragment.class));
        LIST.add(new Item("下拉刷新", PullToRefreshFragment.class));

        LIST.add(new Item("Spring动画", SpringScrollViewFragment.class));
        LIST.add(new Item("CircularReveal动画", CircularRevealActivity.class));
        LIST.add(new Item("翻转动画", ReversalActivity.class));

        LIST.add(new Item("BitmapDrawable", DrawableBitmapFragment.class));
        LIST.add(new Item("LayerDrawable", DrawableLayerFragment.class));
        LIST.add(new Item("RotateDrawable", DrawableRotateFragment.class));
        LIST.add(new Item("SelectorDrawable", DrawableSelectorFragment.class));
        LIST.add(new Item("VectorDrawable", DrawableVectorFragment.class));
        LIST.add(new Item("FishDrawable", FishDrawableFragment.class));
        LIST.add(new Item("SVG China Map", SVGChinaFragment.class));

        LIST.add(new Item("Dialog", DialogsActivity.class));
        LIST.add(new Item("Window Size", RealWindowSizeActivity.class));

        LIST.add(new Item("LayoutInflaterCompat", LayoutInflaterActivity.class));
        LIST.add(new Item("方块动画", SquareAnimationFragment.class));

        LIST.add(new Item("ConstraintLayout 基础", ConstraintLayoutActivity.class));
        LIST.add(new Item("ViewPager 多页同屏", ViewPagerFragment.class));
    }

}