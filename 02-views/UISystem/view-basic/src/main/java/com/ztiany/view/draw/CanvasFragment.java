package com.ztiany.view.draw;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.BaseViewPagerFragment;
import com.ztiany.view.draw.canvas.BitmapMeshView;
import com.ztiany.view.draw.canvas.ClipCanvasView;
import com.ztiany.view.draw.canvas.DashView;
import com.ztiany.view.draw.canvas.DrawArcView;
import com.ztiany.view.draw.canvas.DrawBitmapView;
import com.ztiany.view.draw.canvas.DrawCircle;
import com.ztiany.view.draw.canvas.DrawOvalView;
import com.ztiany.view.draw.canvas.DrawPictureView;
import com.ztiany.view.draw.canvas.DrawPointView;
import com.ztiany.view.draw.canvas.DrawRectView;
import com.ztiany.view.draw.canvas.DrawTextView;
import com.ztiany.view.draw.canvas.MeshView;
import com.ztiany.view.draw.canvas.SaveLayerView;
import com.ztiany.view.draw.canvas.SaveView;
import com.ztiany.view.draw.canvas.ScaleView;
import com.ztiany.view.draw.canvas.SkewView;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-06 12:14
 */
public class CanvasFragment extends BaseViewPagerFragment {

    private String[] titles = {
            "DrawText",
            "DrawArc",
            "DrawBitmap",
            "DrawCircle",
            "DrawOval",
            "DrawPicture",
            "DrawPoint",
            "DrawRect",
            "BitmapMesh",
            "ClipCanvas",
            "DashView",
            "MeshView",
            "SaveLayer",
            "Save",
            "ScaleCanvas",
            "SkewCanvas"
    };

    @Override
    protected PagerAdapter getAdapter() {
        return new CanvasAdapter(titles);
    }

    private class CanvasAdapter extends BasePagerAdapter {

        CanvasAdapter(String[] titles) {
            super(titles);
        }

        @Override
        protected View getItemView(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = new DrawTextView(getContext());
                    break;
                case 1:
                    view = new DrawArcView(getContext());
                    break;
                case 2:
                    view = new DrawBitmapView(getContext());
                    break;
                case 3:
                    view = new DrawCircle(getContext());
                    break;
                case 4:
                    view = new DrawOvalView(getContext());
                    break;
                case 5:
                    view = new DrawPictureView(getContext());
                    break;
                case 6:
                    view = new DrawPointView(getContext());
                    break;
                case 7:
                    view = new DrawRectView(getContext());
                    break;
                case 8:
                    view = new BitmapMeshView(getContext());
                    break;
                case 9:
                    view = new ClipCanvasView(getContext());
                    break;
                case 10:
                    view = new DashView(getContext());
                    break;
                case 11:
                    view = new MeshView(getContext());
                    break;
                case 12:
                    view = new SaveLayerView(getContext());
                    break;
                case 13:
                    view = new SaveView(getContext());
                    break;
                case 14:
                    view = new ScaleView(getContext());
                    break;
                case 15:
                    view = new SkewView(getContext());
                    break;
            }
            return view;
        }
    }
}
