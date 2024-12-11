package me.ztiany.apm.scene.throttle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import me.ztiany.apm.aspect.throttle.ThrottleClick;
import me.ztiany.apm.databinding.ThrottleActivityMainBinding;
import timber.log.Timber;

public class ThrottleClickActivity extends AppCompatActivity {

    private ThrottleActivityMainBinding vb = null;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ThrottleActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());
        setupViews();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                testThrottleClickAnnotation(vb.btnDummy);
                mHandler.postDelayed(this, 100);
            }
        }, 100);
    }

    private void setupViews() {
        vb.btnOpenActivity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThrottleClickActivity.this, ThrottleClickActivity.class));
            }
        });

        vb.btnOpenActivity2.setOnClickListener(v -> {
            startActivity(new Intent(ThrottleClickActivity.this, ThrottleClickActivity.class));
        });
    }

    @ThrottleClick
    private void testThrottleClickAnnotation(View view) {
        Timber.w("testThrottleClickAnnotation is called with view: %s", view.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
