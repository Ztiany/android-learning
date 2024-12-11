package com.ztiany.view.bitmap;

import android.os.Bundle;
import android.view.Menu;

import com.ztiany.view.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2020-06-11 18:16
 */
public class BitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_content);
        if (savedInstanceState == null) {
            showFragment(new ShowBitmapSizeFragment());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("bitmap size")
                .setOnMenuItemClickListener(item -> {
                    showFragment(new ShowBitmapSizeFragment());
                    return true;
                });

        menu.add("BitmapRegionDecoder")
                .setOnMenuItemClickListener(item -> {
                    showFragment(new BitmapRegionDecoderFragment());
                    return true;
                });

        menu.add("inBitmap")
                .setOnMenuItemClickListener(item -> {
                    showFragment(new InBitmapFragment());
                    return true;
                });

        menu.add("MatrixBitmap")
                .setOnMenuItemClickListener(item -> {
                    showFragment(new MatrixBitmapFragment());
                    return true;
                });


        menu.add("PhotoView")
                .setOnMenuItemClickListener(item -> {
                    showFragment(new PhotoViewFragment());
                    return true;
                });

        return true;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_content, fragment, ShowBitmapSizeFragment.class.getName())
                .commit();
    }

}
