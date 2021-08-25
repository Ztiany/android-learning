package com.ztiany.view.draw.path;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;


public class PathMeasureCircleSampleBuilder {

    @SuppressLint("SetTextI18n")
   public static View create(Context context) {
        final PathMeasureAnimView mChild = new PathMeasureAnimView(context);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        Button button = new AppCompatButton(context);
        button.setText("start");
        button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChild.startAnim();
            }
        });
        linearLayout.addView(button);
        linearLayout.addView(mChild);
        return linearLayout;
    }

}
