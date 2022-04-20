package com.ztiany.systemui.uisapmle;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ztiany.systemui.R;
import com.ztiany.systemui.utils.Utils;
import com.ztiany.systemui.utils.systemuihelper.SystemUiHelper;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-07-18 10:54
 */
public class SystemUIActivity extends AppCompatActivity {

    private static final String TAG = SystemUIActivity.class.getSimpleName();
    private SystemUiHelper mSystemUiHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_system_ui);
        Utils.printSize(this);

        mSystemUiHelper = new SystemUiHelper(this,
                SystemUiHelper.LEVEL_IMMERSIVE,
                SystemUiHelper.FLAG_IMMERSIVE_STICKY,
                new SystemUiHelper.OnVisibilityChangeListener() {
                    @Override
                    public void onVisibilityChange(boolean visible) {
                        Log.d(TAG, "visible:" + visible);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSystemUiHelper.hide();
    }

    public void toggle(View view) {
        if (mSystemUiHelper.isShowing()) {
            mSystemUiHelper.hide();
        } else {
            mSystemUiHelper.show();
        }
    }

}
