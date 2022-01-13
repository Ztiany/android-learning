package com.ztiany.systemui.uisapmle;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-19 10:10
 */
public class FullscreenFragment extends Fragment {

    private int mRecordFlags;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new AppCompatButton(getContext());
        textView.setText("Fullscreen");
        textView.setTextColor(Color.BLUE);
        textView.setPadding(100, 100, 100, 100);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SystemUIWithFragmentActivity) getActivity()).showFullscreenFragment();
            }
        });
        return textView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        mRecordFlags = attributes.flags;
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WindowManager.LayoutParams attributes = getActivity().getWindow().getAttributes();
        attributes.flags = mRecordFlags;
        getActivity().getWindow().setAttributes(attributes);
    }

}
