package com.ztiany.systemui.uisapmle;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ztiany.systemui.R;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-18 10:55
 */
public class SystemUIWithFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_with_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, new NormalFragment(), NormalFragment.class.getName())
                    .commit();
        }
    }

    public void showFullscreenFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_container, new FullscreenFragment(), FullscreenFragment.class.getName())
                .addToBackStack("A")
                .commit();
    }

}
