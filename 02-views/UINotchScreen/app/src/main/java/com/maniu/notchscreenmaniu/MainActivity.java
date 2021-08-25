package com.maniu.notchscreenmaniu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maniu.notchscreenmaniu.screen.BaseActivity;
import com.maniu.notchscreenmaniu.screen.NotchProperty;
import com.maniu.notchscreenmaniu.screen.NotchTools;
import com.maniu.notchscreenmaniu.screen.interfaces.OnNotchCallBack;

public class MainActivity extends BaseActivity implements OnNotchCallBack {
    NotchTools ntchTools = new NotchTools();
    private ImageView mBackView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBackView = findViewById(R.id.img_back);
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ntchTools.fullScreenDontUseNotch(this);

    }

    @Override
    public void onNotchPropertyCallback( NotchProperty notchProperty) {
//        移动我们控件  底层 +  不想去使用 刘海屏   全面
        int marginTop = notchProperty.getNotchHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mBackView.getLayoutParams();
        layoutParams.topMargin += marginTop;
        mBackView.setLayoutParams(layoutParams);
    }
}