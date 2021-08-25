package com.ztiany.base;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ztiany.base.presentation.ImageLoader;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 11:57
 */
class GlideImageLoader implements ImageLoader {

    @Inject
    GlideImageLoader() {

    }

    @Override
    public void display(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
