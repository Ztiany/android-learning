package com.ztiany.view.bitmap;

import android.widget.FrameLayout;

import com.ztiany.view.BaseFrameFragment;
import com.ztiany.view.R;
import com.ztiany.view.bitmap.views.ZoomImageView;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-12 15:34
 */
public class MatrixBitmapFragment extends BaseFrameFragment {

    @Override
    protected void buildView(FrameLayout frameLayout) {
        ZoomImageView imageView = new ZoomImageView(getContext());
        imageView.setImageResource(R.drawable.img_girl_01);
        frameLayout.addView(imageView, newMM());
    }

}
