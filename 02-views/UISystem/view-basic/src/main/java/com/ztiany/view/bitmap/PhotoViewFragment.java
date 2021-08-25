package com.ztiany.view.bitmap;

import android.widget.FrameLayout;

import com.ztiany.view.BaseFrameFragment;
import com.ztiany.view.bitmap.views.PhotoView;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-12 15:34
 */
public class PhotoViewFragment extends BaseFrameFragment {

    @Override
    protected void buildView(FrameLayout frameLayout) {
        PhotoView imageView = new PhotoView(getContext());
        frameLayout.addView(imageView, newMM());
    }

}
