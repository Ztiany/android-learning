package com.ztiany.view.draw.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-12 11:08
 */
public class Camera3DFragment extends Fragment {

    private Handler mHandler = new Handler();
    private Roll3DView mRotate3DView1;
    private Roll3DView mRotate3DView2;
    private Roll3DView mRotate3DView3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_3d_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mRotate3DView1 = (Roll3DView) view.findViewById(R.id.rotate_3d_view1);
        mRotate3DView2 = (Roll3DView) view.findViewById(R.id.rotate_3d_view2);
        mRotate3DView3 = (Roll3DView) view.findViewById(R.id.rotate_3d_view3);

        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.img1);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.img2);
        Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.img3);
        Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.img4);

        mRotate3DView1.addImageBitmap(bitmap1);
        mRotate3DView1.addImageBitmap(bitmap2);
        mRotate3DView1.addImageBitmap(bitmap3);
        mRotate3DView1.addImageBitmap(bitmap4);

        mRotate3DView2.addImageBitmap(bitmap1);
        mRotate3DView2.addImageBitmap(bitmap2);
        mRotate3DView2.addImageBitmap(bitmap3);
        mRotate3DView2.addImageBitmap(bitmap4);

        mRotate3DView3.addImageBitmap(bitmap1);
        mRotate3DView3.addImageBitmap(bitmap2);
        mRotate3DView3.addImageBitmap(bitmap3);
        mRotate3DView3.addImageBitmap(bitmap4);

        mRotate3DView1.setRollDuration(2000);
        mRotate3DView2.setRollDuration(2000);
        mRotate3DView3.setRollDuration(2000);

        mHandler.postDelayed(mRunnable, 2000);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mRotate3DView1.toNext();
            mRotate3DView2.toNext();
            mRotate3DView3.toNext();
            mHandler.postDelayed(this, 2000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
