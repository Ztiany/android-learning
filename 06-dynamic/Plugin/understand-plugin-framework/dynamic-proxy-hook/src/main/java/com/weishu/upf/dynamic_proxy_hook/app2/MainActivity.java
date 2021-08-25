package com.weishu.upf.dynamic_proxy_hook.app2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.weishu.upf.dynamic_proxy_hook.app2.hook.HookHelper;

import java.lang.reflect.Field;

/**
 * @author weishu
 * @date 16/1/28
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //支持Activity直接跳转请在这里Hook
        hookActivityInstrumentation();

        Button tv = new Button(this);
        tv.setText("测试界面");

        setContentView(tv);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("http://www.baidu.com"));

                // 注意这里使用的ApplicationContext 启动的Activity
                // 因为Activity对象的startActivity使用的并不是ContextImpl的mInstrumentation
                // 而是自己的mInstrumentation, 如果你需要这样, 可以自己Hook
                // 比较简单, 直接替换这个Activity的此字段即可.
                //getApplicationContext().startActivity(intent);
                startActivity(intent);
            }
        });
    }

    private void hookActivityInstrumentation() {
        HookHelper.attachContext(this);
    }

}
