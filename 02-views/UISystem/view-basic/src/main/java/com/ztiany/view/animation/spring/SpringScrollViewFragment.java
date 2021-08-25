package com.ztiany.view.animation.spring;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztiany.view.R;
import com.ztiany.view.utils.UnitConverter;

import java.util.Random;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 18:46
 */
public class SpringScrollViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.animation_spring_scroll_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout content = (LinearLayout) view.findViewById(R.id.ll_content);
        TextView textView;
        Random random = new Random();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding = UnitConverter.dpToPx(20);
        for (int i = 0; i < 20; i++) {
            textView = new TextView(getContext());
            textView.setPadding(padding,padding,padding,padding);
            textView.setBackgroundColor(Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            textView.setText(String.valueOf(i));
            content.addView(textView,layoutParams);
        }
    }
}
