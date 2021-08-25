package com.ztiany.view.drawable;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-01-27 15:08
 */
public class DrawableVectorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.drawable_fragment_vector, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        assert view != null;
        ImageView imageView = view.findViewById(R.id.tv_animated_cpu);
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

}
