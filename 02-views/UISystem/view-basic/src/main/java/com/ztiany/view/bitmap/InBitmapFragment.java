package com.ztiany.view.bitmap;

import android.widget.FrameLayout;

import com.ztiany.view.BaseFrameFragment;
import com.ztiany.view.bitmap.views.InBitmapLargeImageView;

import java.io.IOException;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-12 15:24
 */
public class InBitmapFragment extends BaseFrameFragment {

    @Override
    protected void buildView(FrameLayout frameLayout) {
        InBitmapLargeImageView inBitmapLargeImageView = new InBitmapLargeImageView(getContext());
        try {
            inBitmapLargeImageView.setImage(requireContext().getAssets().open("huge_img_android.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frameLayout.addView(inBitmapLargeImageView, newMM());
    }

}
