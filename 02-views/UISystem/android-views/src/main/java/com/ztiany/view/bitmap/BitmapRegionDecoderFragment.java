package com.ztiany.view.bitmap;

import android.widget.FrameLayout;

import com.ztiany.view.BaseFrameFragment;
import com.ztiany.view.bitmap.views.BitmapRegionDecoderLargeImageView;

import java.io.IOException;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-12 15:24
 */
public class BitmapRegionDecoderFragment extends BaseFrameFragment {

    @Override
    protected void buildView(FrameLayout frameLayout) {
        BitmapRegionDecoderLargeImageView bitmapRegionDecoderLargeImageView = new BitmapRegionDecoderLargeImageView(getContext());
        try {
            bitmapRegionDecoderLargeImageView.setImageStream(requireContext().getAssets().open("huge_img_qmsht.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frameLayout.addView(bitmapRegionDecoderLargeImageView, newMM());
    }

}