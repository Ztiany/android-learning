package com.ztiany.view.draw.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 12:33
 */
public class Camera3DTheoryFragment extends Fragment {

    private Camera3DTheoryView mCamera3DTheoryView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_3d_theory, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCamera3DTheoryView = (Camera3DTheoryView) view.findViewById(R.id.camera_3d_theory);
        ((SeekBar)view.findViewById(R.id.seek_bar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCamera3DTheoryView.onProgressChanged(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        view.findViewById(R.id.show_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera3DTheoryView.showOne();
            }
        });

        view.findViewById(R.id.show_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera3DTheoryView.showTwo();
            }
        });

        view.findViewById(R.id.show_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera3DTheoryView.showAll();
            }
        });
    }
}
