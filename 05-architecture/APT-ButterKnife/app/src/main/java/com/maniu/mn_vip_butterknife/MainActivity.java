package com.maniu.mn_vip_butterknife;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.maniu.annotation.BindView;
import com.maniu.annotation.OnClick;
import com.maniu.butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView1:
                Toast.makeText(MainActivity.this,"1111111111111111111111",Toast.LENGTH_LONG).show();
                break;
            case R.id.textView2:
                Toast.makeText(MainActivity.this,"222222222222222222222222",Toast.LENGTH_LONG).show();
                break;
            case R.id.textView3:
                Toast.makeText(MainActivity.this,"33333333333333333333333",Toast.LENGTH_LONG).show();
                break;
            case R.id.textView4:
                Toast.makeText(MainActivity.this,"4444444444444444444444444",Toast.LENGTH_LONG).show();
                break;
            case R.id.textView5:
                Toast.makeText(MainActivity.this,"55555555555555555555555555",Toast.LENGTH_LONG).show();
                break;
        }
    }
}
