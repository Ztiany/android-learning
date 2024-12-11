package com.ztiany.view.draw.color;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ztiany.view.R;

import java.util.Arrays;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ColorMatrixFilterFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = ColorMatrixFilterFragment.class.getSimpleName();

    private Bitmap mBitmap;
    private Bitmap mDesBitmap;
    private Canvas mCanvas;

    private Paint mPaint;
    private ColorMatrix mColorMatrix;
    private float mAlpha;
    private float mRed;
    private float mGreen;
    private float mBlue;

    protected ImageView mImageView;
    protected SeekBar mAlphaSb;
    protected SeekBar mRedSb;
    protected SeekBar mGreenSb;
    protected SeekBar mBlueSb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.color_matrix_fragment, container, false);
    }

    private void initRes() {
        mAlpha = 128;
        mRed = 128;
        mGreen = 128;
        mBlue = 128;
        mAlphaSb.setProgress(128);
        mRedSb.setProgress(128);
        mGreenSb.setProgress(128);
        mBlueSb.setProgress(128);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mColorMatrix = new ColorMatrix();
        mImageView = (ImageView) view.findViewById(R.id.frag_color_matrix_iv);
        mAlphaSb = (SeekBar) view.findViewById(R.id.frag_color_matrix_alpha_sb);
        mRedSb = (SeekBar) view.findViewById(R.id.frag_color_matrix_red_sb);
        mGreenSb = (SeekBar) view.findViewById(R.id.frag_color_matrix_green_sb);
        mBlueSb = (SeekBar) view.findViewById(R.id.frag_color_matrix_blue_sb);
        init();
    }

    private void init() {

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_gril_02);

        mImageView.setImageBitmap(mBitmap);
        mDesBitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        mCanvas = new Canvas(mDesBitmap);

        mAlphaSb.setOnSeekBarChangeListener(this);
        mRedSb.setOnSeekBarChangeListener(this);
        mGreenSb.setOnSeekBarChangeListener(this);
        mBlueSb.setOnSeekBarChangeListener(this);

        View.OnClickListener effectClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanvas == null) {
                    return;
                }

                float[] matrix = null;

                switch (v.getId()) {
                    case R.id.effect1: {
                        matrix = effect1;
                        break;
                    }
                    case R.id.effect2: {
                        matrix = effect2;
                        break;
                    }
                    case R.id.effect3: {
                        matrix = effect3;
                        break;
                    }
                    case R.id.effect4: {
                        matrix = effect4;
                        break;
                    }
                    case R.id.effect5: {
                        matrix = effect5;
                        break;
                    }
                    case R.id.effect6: {
                        setBitmapPixels(mBitmap, mDesBitmap);
                        mImageView.setImageBitmap(mDesBitmap);
                        return;
                    }
                }
                if (matrix == null) {
                    return;
                }
                mColorMatrix.set(matrix);
                ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(mColorMatrix);
                mPaint.setColorFilter(colorMatrixColorFilter);
                mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
                mImageView.setImageBitmap(mDesBitmap);
            }
        };

        assert getView() != null;
        getView().findViewById(R.id.effect1).setOnClickListener(effectClickListener);
        getView().findViewById(R.id.effect2).setOnClickListener(effectClickListener);
        getView().findViewById(R.id.effect3).setOnClickListener(effectClickListener);
        getView().findViewById(R.id.effect4).setOnClickListener(effectClickListener);
        getView().findViewById(R.id.effect5).setOnClickListener(effectClickListener);
        getView().findViewById(R.id.effect6).setOnClickListener(effectClickListener);
        initRes();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mCanvas == null) {
            return;
        }
        switch (seekBar.getId()) {
            case R.id.frag_color_matrix_alpha_sb:
                mAlpha = progress;
                break;
            case R.id.frag_color_matrix_red_sb:
                mRed = progress;
                break;
            case R.id.frag_color_matrix_green_sb:
                mGreen = progress;
                break;
            case R.id.frag_color_matrix_blue_sb:
                mBlue = progress;
                break;
        }

        float[] matrix = new float[]{
                mRed * 1.0F / 128, 0, 0, 0, 0,
                0, mGreen * 1.0F / 128, 0, 0, 0,
                0, 0, mBlue * 1.0F / 128, 0, 0,
                0, 0, 0, mAlpha * 1.0F / 128, 0,
        };

        Log.d(TAG, Arrays.toString(matrix));
        mColorMatrix.set(matrix);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(mColorMatrix);
        mPaint.setColorFilter(colorMatrixColorFilter);
        mCanvas.drawBitmap(mBitmap, 0, 0, mPaint);
        mImageView.setImageBitmap(mDesBitmap);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    float[] effect1 = {
            0.33F, 0.59F, 0.11F, 0, 0,
            0.33F, 0.59F, 0.11F, 0, 0,
            0.33F, 0.59F, 0.11F, 0, 0,
            0, 0, 0, 1, 0,
    };
    float[] effect2 = {
            -1, 0, 0, 1, 1,
            0, -1, 0, 1, 1,
            0, 0, -1, 1, 1,
            0, 0, 0, 1, 0,
    };
    float[] effect3 = {
            0.393F, 0.769F, 0.189F, 0, 0,
            0.349F, 0.686F, 0.168F, 0, 0,
            0.272F, 0.534F, 0.131F, 0, 0,
            0, 0, 0, 1, 0,
    };
    float[] effect4 = {
            1.5F, 1.5F, 1.5F, 0, -1,
            1.5F, 1.5F, 1.5F, 0, -1,
            1.5F, 1.5F, 1.5F, 0, -1,
            0, 0, 0, 1, 0,
    };
    float[] effect5 = {
            1.438F, -0.122F, -0.016F, 0, -0.03F,
            -0.62F, 1.378F, -0.16F, 0, -0.05F,
            -0.062F, -0.122F, 1.438F, 0, -0.02F,
            0, 0, 0, 1, 0,
    };


    private void setBitmapPixels(Bitmap bitmap, Bitmap desBitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int length = pixels.length;
        int color;
        int r, g, b, a;
        for (int i = 0; i < length; i++) {
            color = pixels[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            r = r > 255 ? 255 : r < 0 ? 0 : r;
            g = g > 255 ? 255 : g < 0 ? 0 : g;
            b = b > 255 ? 255 : b < 0 ? 0 : b;
            a = a > 255 ? 255 : a < 0 ? 0 : a;
            pixels[i] = Color.argb(a, r, g, b);
        }
        desBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

}
