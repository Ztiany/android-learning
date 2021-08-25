package com.ztiany.view.draw.color;

import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class MaskFilterFragment extends Fragment {


    private MaskFilter mMaskFilter;
    private ViewGroup mBlurVg;
    private ViewGroup mEmbossVg;
    private float mBlurProgress = 1F;
    private BlurMaskFilter.Blur mBlur;
    float[] mDirection;
    float mAmbient, mSpecular, mBlurRadius;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mask_filter_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, 1, 0, "模糊滤镜").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mEmbossVg.setVisibility(View.GONE);
                mBlurVg.setVisibility(View.VISIBLE);
                return true;
            }
        });
        menu.add(Menu.NONE, 2, 0, "浮雕滤镜").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mEmbossVg.setVisibility(View.VISIBLE);
                mBlurVg.setVisibility(View.GONE);
                return true;
            }

        });
        menu.add(Menu.NONE, 3, 0, "无效果").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mEmbossVg.setVisibility(View.GONE);
                mBlurVg.setVisibility(View.GONE);
                mMaskFilter.noMaskFilter();
                return true;
            }
        });
    }

    private void findViews() {
        mMaskFilter = (MaskFilter) findViewById(R.id.frag_mask_filter);
        mBlurVg = (ViewGroup) findViewById(R.id.blur_ll);
        mEmbossVg = (ViewGroup) findViewById(R.id.emboss_ll);
        setEmbossListener();
        setBlurListener();
    }

    private View findViewById(int id) {
        assert getView() != null;
        return getView().findViewById(id);
    }

    private void setEmbossListener() {
        SeekBar xSb = (SeekBar) findViewById(R.id.direction_x);
        SeekBar ySb = (SeekBar) findViewById(R.id.direction_y);
        SeekBar zSb = (SeekBar) findViewById(R.id.direction_z);
        SeekBar ambient = (SeekBar) findViewById(R.id.ambient);//0-1
        SeekBar specular = (SeekBar) findViewById(R.id.specular);//0-1
        SeekBar blurRadius = (SeekBar) findViewById(R.id.blurRadius);

        final TextView xTv = (TextView) findViewById(R.id.direction_x_tv);
        final TextView yTv = (TextView) findViewById(R.id.direction_y_tv);
        final TextView zTv = (TextView) findViewById(R.id.direction_z_tv);
        final TextView ambientTv = (TextView) findViewById(R.id.ambient_tv);
        final TextView specularTv = (TextView) findViewById(R.id.specular_tv);
        final TextView blurRadiusTv = (TextView) findViewById(R.id.blurRadius_tv);

        mDirection = new float[3];

        mAmbient = 0.1F;
        mSpecular = 0.1F;
        mBlurRadius = 0;


        SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.direction_x: {
                        mDirection[0] = progress;
                        xTv.setText(String.valueOf(progress));

                        break;
                    }
                    case R.id.direction_y: {
                        mDirection[1] = progress;
                        yTv.setText(String.valueOf(progress));

                        break;
                    }
                    case R.id.direction_z: {
                        mDirection[2] = progress;
                        zTv.setText(String.valueOf(progress));

                        break;
                    }
                    case R.id.ambient: {
                        mAmbient = progress * 1.0F / 100;
                        ambientTv.setText(String.valueOf(mAmbient));

                        break;
                    }
                    case R.id.specular: {
                        mSpecular = progress * 1.0F / 100;
                        specularTv.setText(String.valueOf(mSpecular));

                        break;
                    }
                    case R.id.blurRadius: {
                        mBlurRadius = progress;
                        blurRadiusTv.setText(String.valueOf(mBlurRadius));

                        break;
                    }
                }
                mMaskFilter.setEmbossMaskFilter(mDirection, mAmbient, mSpecular, mBlurRadius);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        xSb.setOnSeekBarChangeListener(seekBarChangeListener);
        ySb.setOnSeekBarChangeListener(seekBarChangeListener);
        zSb.setOnSeekBarChangeListener(seekBarChangeListener);
        ambient.setOnSeekBarChangeListener(seekBarChangeListener);
        specular.setOnSeekBarChangeListener(seekBarChangeListener);
        blurRadius.setOnSeekBarChangeListener(seekBarChangeListener);


    }

    private void setBlurListener() {
        SeekBar seekBar = (SeekBar) findViewById(R.id.blur_seek_bar);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.blur_rg);
        final TextView bTv = (TextView) findViewById(R.id.blur_tv);


        mBlurProgress = 1F;
        mBlur = BlurMaskFilter.Blur.NORMAL;

        radioGroup.check(R.id.blur_normal);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (progress > 0) {
                    mBlurProgress = progress;
                    bTv.setText(String.valueOf(mBlurProgress));
                }

                if (mBlur != null) {
                    mMaskFilter.setBlurMaskFilter(mBlurProgress, mBlur);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.blur_normal:
                        mBlur = BlurMaskFilter.Blur.NORMAL;
                        break;
                    case R.id.blur_inner:
                        mBlur = BlurMaskFilter.Blur.INNER;
                        break;
                    case R.id.blur_outer:
                        mBlur = BlurMaskFilter.Blur.OUTER;
                        break;
                    case R.id.blur_solid:
                        mBlur = BlurMaskFilter.Blur.SOLID;
                        break;
                }
                if (mBlur != null) {
                    mMaskFilter.setBlurMaskFilter(mBlurProgress, mBlur);
                }

            }
        });
    }


}
