package com.ztiany.view;

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
import com.ztiany.view.drawable.DrawablesActivity;
import com.ztiany.view.inflater.LayoutInflaterActivity;
import com.ztiany.view.material.MaterialComponentActivity;
import com.ztiany.view.scroll.ScrollFragment;
import com.ztiany.view.scroll.sticky.StickyNavigationFragment;
import com.ztiany.view.window.RealWindowSizeActivity;

import java.util.List;

public class MainActivity extends BaseMainActivity {

    @Override
    protected void provideItems(List<Item> items) {
        items.add(new Item("RecyclerView 学习", RecyclerViewActivity.class));
        items.add(new Item("Material 组件学习", MaterialComponentActivity.class));
        items.add(new Item("Drawable 研究", DrawablesActivity.class));
        items.add(new Item("HenCoderPlus 练习", HenCoderPlusFragment.class));

        items.add(new Item("Bitmap 研究", BitmapActivity.class));
        items.add(new Item("事件 & 滑动", ScrollFragment.class));
        items.add(new Item("Sticky Navigation", StickyNavigationFragment.class));
        items.add(new Item("Canvas 绘制", CanvasFragment.class));
        items.add(new Item("矩阵", MatrixFragment.class));
        items.add(new Item("ColorMatrixFilter", ColorMatrixFilterFragment.class));
        items.add(new Item("MaskFilter", MaskFilterFragment.class));
        items.add(new Item("Path &贝塞尔曲线", PathFragment.class));
        items.add(new Item("Camera 变换", CameraDemoViewFragment.class));
        items.add(new Item("Camera3D View", Camera3DFragment.class));
        items.add(new Item("Camera3D 原理", Camera3DTheoryFragment.class));
        items.add(new Item("文字渐变", SimpleTextGradualFragment.class));
        items.add(new Item("文字测量", TextMeasureFragment.class));
        items.add(new Item("文字渐变 ViewPager", TextGradualViewPagerFragment.class));
        items.add(new Item("过度绘制", OverDrawFragment.class));

        items.add(new Item("ViewDragHelper 使用", ViewDragHelperFragment.class));
        items.add(new Item("自定义 View", CustomViewFragment.class));
        items.add(new Item("消息的拖拽", MessageDragFragment.class));
        items.add(new Item("流式布局", FlowLayoutFragment.class));
        items.add(new Item("下拉刷新", PullToRefreshFragment.class));

        items.add(new Item("Spring 动画", SpringScrollViewFragment.class));
        items.add(new Item("CircularReveal 动画", CircularRevealActivity.class));
        items.add(new Item("翻转动画", ReversalActivity.class));
        items.add(new Item("方块动画", SquareAnimationFragment.class));

        items.add(new Item("Dialog", DialogsActivity.class));
        items.add(new Item("Window 研究", RealWindowSizeActivity.class));

        items.add(new Item("LayoutInflaterCompat", LayoutInflaterActivity.class));

        items.add(new Item("ConstraintLayout 基础", ConstraintLayoutActivity.class));
        items.add(new Item("ViewPager 多页同屏", ViewPagerFragment.class));

        items.add(new Item("子线程更新 UI", ViewRootActivity.class));
    }

}