package com.ztiany.view.courses.hencoderplus;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ztiany.view.R;
import com.ztiany.view.courses.hencoderplus.animation.ProvinceView;
import com.ztiany.view.courses.hencoderplus.camera.AnimationCameraView;
import com.ztiany.view.courses.hencoderplus.camera.CameraView;
import com.ztiany.view.courses.hencoderplus.draw.AvatarView;
import com.ztiany.view.courses.hencoderplus.draw.AvatarView2;
import com.ztiany.view.courses.hencoderplus.draw.FillTypeView;
import com.ztiany.view.courses.hencoderplus.draw.PieChart;
import com.ztiany.view.courses.hencoderplus.drawable.DrawableView;
import com.ztiany.view.courses.hencoderplus.measure.SquareImageView5;
import com.ztiany.view.courses.hencoderplus.text.ImageTextView;
import com.ztiany.view.courses.hencoderplus.text.MultilineTextView5;
import com.ztiany.view.courses.hencoderplus.text.SportView;
import com.ztiany.view.courses.hencoderplus.text.SportView5;
import com.ztiany.view.courses.hencoderplus.touch.MultiTouchView1;
import com.ztiany.view.courses.hencoderplus.touch.MultiTouchView2;
import com.ztiany.view.courses.hencoderplus.touch.MultiTouchView3;
import com.ztiany.view.courses.hencoderplus.touch.ScalableImageView;
import com.ztiany.view.courses.hencoderplus.touch.ScalableImageView5;
import com.ztiany.view.courses.hencoderplus.xfermod.XfermodeView;
import com.ztiany.view.draw.canvas.DashView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-02 15:08
 */
public class HenCoderPlusFragment extends Fragment {

    private final List<Pair<String, ? extends View>> items = new ArrayList<>();
    private FrameLayout mFrameLayout;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
        initViews();
    }

    private void initViews() {
        /*绘制*/
        items.add(new Pair<>("AvatarView", new AvatarView(requireContext())));
        items.add(new Pair<>("AvatarView2", new AvatarView2(requireContext())));
        items.add(new Pair<>("DashView", new DashView(requireContext())));
        items.add(new Pair<>("FillTypeView", new FillTypeView(requireContext())));
        items.add(new Pair<>("PieChart", new PieChart(requireContext(), null)));

        /*Xfermode*/
        items.add(new Pair<>("XfermodeView", new XfermodeView(requireContext(), null)));

        /*文字绘制与测量*/
        items.add(new Pair<>("SportView", new SportView(requireContext())));
        items.add(new Pair<>("ImageTextView", new ImageTextView(requireContext())));
        items.add(new Pair<>("SportView5", new SportView5(requireContext())));
        items.add(new Pair<>("MultilineTextView5", new MultilineTextView5(requireContext())));

        //裁剪与 Camera
        items.add(new Pair<>("CameraView", new CameraView(requireContext())));
        items.add(new Pair<>("AnimationCameraView", new AnimationCameraView(requireContext())));

        //动画与硬件加速
        items.add(new Pair<>("ProvinceView", new ProvinceView(requireContext())));

        //Drawable 和 Bitmap
        items.add(new Pair<>("DrawableView", new DrawableView(requireContext())));

        //自定义 MaterialEditText
        items.add(new Pair<>("MaterialEditText", View.inflate(requireContext(), R.layout.hencoder_layout_material_edittext, null)));

        //View 的绘制与布局流程
        items.add(new Pair<>("TagLayout5", View.inflate(requireContext(), R.layout.hencoder_taglayout5, null)));
        items.add(new Pair<>("SquareImageView5", new SquareImageView5(requireContext(), null)));

        //View触摸反馈
        items.add(new Pair<>("ScalableImageView", new ScalableImageView(getContext(), null)));
        items.add(new Pair<>("ScalableImageView5", new ScalableImageView5(requireContext(), null)));

        //多点触控
        items.add(new Pair<>("MultiTouchView1", new MultiTouchView1(getContext(), null)));
        items.add(new Pair<>("MultiTouchView2", new MultiTouchView2(getContext(), null)));
        items.add(new Pair<>("MultiTouchView3", new MultiTouchView3(getContext(), null)));

        //ViewGroup 触摸反馈
        items.add(new Pair<>("TwoPager", View.inflate(requireContext(), R.layout.hencoder_layout_two_pager, null)));

        //View 的 Drag
        items.add(new Pair<>("drag1: ViewGradHelper", View.inflate(requireContext(), R.layout.hencoder_layout_drag_helper_grid_view, null)));
        items.add(new Pair<>("drag2: SystemDrag", View.inflate(requireContext(), R.layout.hencoder_drag_listener_grid_view, null)));
        items.add(new Pair<>("drag3: Collect", View.inflate(requireContext(), R.layout.hencoder_drag_to_collect, null)));
        items.add(new Pair<>("drag4: UPAndDown", View.inflate(requireContext(), R.layout.hencoder_drag_up_down, null)));

        //嵌套滑动
        items.add(new Pair<>("nested: ImageView", View.inflate(requireContext(), R.layout.hencoder_nested_scroll, null)));
        items.add(new Pair<>("nested: ScrollView", View.inflate(requireContext(), R.layout.hencoder_layout_nested_scroll_view, null)));

        //ConstraintLayout
        //过渡动画及MotionLayout
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFrameLayout = new FrameLayout(requireContext());
        return mFrameLayout;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, @NotNull MenuInflater inflater) {
        for (final Pair<String, ? extends View> pair : items) {
            menu.add(pair.first).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mFrameLayout.removeAllViews();
                    int matchParent = ViewGroup.LayoutParams.MATCH_PARENT;
                    mFrameLayout.addView(pair.second, new FrameLayout.LayoutParams(matchParent, matchParent));
                    return true;
                }
            });
        }
    }

}