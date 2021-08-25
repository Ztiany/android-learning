package com.ztiany.view.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ztiany.view.utils.UnitConverter;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-11 18:18
 */
public class ShowBitmapSizeFragment extends Fragment {

    private static final String TAG = "ShowBitmapSizeFragment";

    private TextView mTextView;
    private Bitmap mBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout frameLayout = new FrameLayout(requireContext());

        try {
            mBitmap = BitmapFactory.decodeStream(getResources().getAssets().open("img_ball.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mTextView = new TextView(requireContext());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        params.leftMargin = UnitConverter.dpToPx(10);
        params.rightMargin = UnitConverter.dpToPx(10);
        params.topMargin = UnitConverter.dpToPx(10);
        params.bottomMargin = UnitConverter.dpToPx(10);

        frameLayout.addView(mTextView, params);
        return frameLayout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int bitmapSize = getBitmapSize();

        String equal = " = ";
        String comma = " , ";
        String newLine = "\n";

        String text =
                "realWidth * realHeight * 4" + equal + (mBitmap.getWidth() + "*" + mBitmap.getHeight() + "*" + 4) + equal + (mBitmap.getWidth() * mBitmap.getHeight() * 4) + newLine
                        + "in asset real size" + equal + 2274064 + comma + "formatter " + equal + Formatter.formatFileSize(requireContext(), 2274064) + newLine
                        + "in xxxhdpi real size" + equal + 1281424 + comma + "formatter " + equal + Formatter.formatFileSize(requireContext(), 1281424) + newLine
                        + "in hdpi real size" + equal + 9096256 + comma + "formatter " + equal + Formatter.formatFileSize(requireContext(), 9096256) + newLine
                        + "in mdpi real size" + equal + 20466576 + comma + "formatter " + equal + Formatter.formatFileSize(requireContext(), 20466576) + newLine
                        + "density " + equal + getResources().getDisplayMetrics().density
                        + comma + "densityDpi " + equal + getResources().getDisplayMetrics().densityDpi + newLine
                        + "widthPixels " + equal + getResources().getDisplayMetrics().widthPixels
                        + comma + "heightPixels " + equal + getResources().getDisplayMetrics().heightPixels;

        mTextView.setText(text);

        Log.d(TAG, text);
    }

    private int getBitmapSize() {
        int bitmapSize;
        if (Build.VERSION.SDK_INT >= 19) {
            bitmapSize = mBitmap.getAllocationByteCount();
        } else {
            bitmapSize = mBitmap.getHeight() * mBitmap.getRowBytes();
        }
        return bitmapSize;
    }

}
