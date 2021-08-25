package com.ztiany.view.draw.camera;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 00:16
 */
public class CameraDemoViewFragment extends Fragment {

    private CameraDemoView mCameraDemoView;
    private TextView mRotateX;
    private TextView mRotateY;
    private TextView mRotateZ;
    private TextView mTranslateZ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_demo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCameraDemoView = (CameraDemoView) view.findViewById(R.id.camera_demo_view);
        mRotateX = (TextView) view.findViewById(R.id.camera_rotate_x);
        mRotateY = (TextView) view.findViewById(R.id.camera_rotate_y);
        mRotateZ = (TextView) view.findViewById(R.id.camera_rotate_z);
        mTranslateZ = (TextView) view.findViewById(R.id.camera_rotate_tz);

        view.findViewById(R.id.add_rotate_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateX(true);
                mRotateX.setText(String.valueOf(mCameraDemoView.getRotateX()));
            }
        });
        view.findViewById(R.id.add_rotate_y).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateY(true);
                mRotateY.setText(String.valueOf(mCameraDemoView.getRotateY()));
            }
        });
        view.findViewById(R.id.add_rotate_z).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateZ(true);
                mRotateZ.setText(String.valueOf(mCameraDemoView.getRotateZ()));
            }
        });
        view.findViewById(R.id.add_rotate_tz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setTranslateZ(true);
                mTranslateZ.setText(String.valueOf(mCameraDemoView.getTranslateZ()));
            }
        });


        view.findViewById(R.id.sub_rotate_x).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateX(false);
                mRotateX.setText(String.valueOf(mCameraDemoView.getRotateX()));
            }
        });
        view.findViewById(R.id.sub_rotate_y).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateY(false);
                mRotateY.setText(String.valueOf(mCameraDemoView.getRotateY()));
            }
        });
        view.findViewById(R.id.sub_rotate_z).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setRotateZ(false);
                mRotateZ.setText(String.valueOf(mCameraDemoView.getRotateZ()));
            }
        });
        view.findViewById(R.id.sub_rotate_tz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.setTranslateZ(false);
                mTranslateZ.setText(String.valueOf(mCameraDemoView.getTranslateZ()));
            }
        });

        view.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.reset();
                mRotateX.setText(String.valueOf(mCameraDemoView.getRotateX()));
                mRotateY.setText(String.valueOf(mCameraDemoView.getRotateY()));
                mRotateZ.setText(String.valueOf(mCameraDemoView.getRotateZ()));
                mTranslateZ.setText(String.valueOf(mCameraDemoView.getTranslateZ()));
            }
        });

        view.findViewById(R.id.matrix_canvas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.useMatrixWithCanvas();
            }
        });

        view.findViewById(R.id.matrix_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDemoView.useMatrixWithBitmap();
            }
        });

    }

}
