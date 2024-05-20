package com.ztiany.view.custom.flow_layout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Random;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-22 18:54
 */
public class FlowLayoutFragment extends Fragment {

    private Random mRandom = new Random();

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText editText = new EditText(getContext());
        editText.setText("Enter a Text");
        linearLayout.addView(editText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        Button button = new Button(getContext());
        linearLayout.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText("add");

        ScrollView scrollView = new ScrollView(getContext());
        final FlowLayout flowLayout = new FlowLayout(getContext());
        LinearLayout innerLayout = new LinearLayout(getContext());
        innerLayout.addView(flowLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(innerLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearLayout.addView(scrollView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = new TextView(getContext());
                textView.setText(editText.getText());
                int padding = mRandom.nextInt(20);
                textView.setPadding(padding, padding, padding, padding);
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                marginLayoutParams.bottomMargin = mRandom.nextInt(30);
                marginLayoutParams.topMargin = mRandom.nextInt(30);
                marginLayoutParams.leftMargin = mRandom.nextInt(30);
                marginLayoutParams.rightMargin = mRandom.nextInt(30);
                textView.setBackgroundColor(Color.argb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
                flowLayout.addView(textView,marginLayoutParams);
            }
        });

        return linearLayout;
    }
}
