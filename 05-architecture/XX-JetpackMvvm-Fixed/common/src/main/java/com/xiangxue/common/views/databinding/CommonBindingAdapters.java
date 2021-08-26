package com.xiangxue.common.views.databinding;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CommonBindingAdapters {

    @BindingAdapter("loadImageUrl")
    public static void loadImageUrl(ImageView imageView, String pitureUrl){
        if(!TextUtils.isEmpty(pitureUrl)) {
            Glide.with(imageView.getContext())
                    .load(pitureUrl)
                    .transition(withCrossFade())
                    .into(imageView);
        }
    }
}
