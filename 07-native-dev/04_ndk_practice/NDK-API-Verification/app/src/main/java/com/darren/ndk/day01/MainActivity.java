package com.darren.ndk.day01;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);

        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Signature[] signatures = packageInfo.signatures;
        Log.e("TAG",signatures[0].toCharsString());
        // 作为参数给到服务器，服务也会生成同样的密文，然后对加密的字符串进行比较
        tv.setText(SignatureUtils.signatureParams("userName=240336124&userPwd=123456"));
        // 安全不？能不能反编译破解？增加了一丁点难度，防止别人调试 xposed
    }
}
