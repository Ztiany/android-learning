package com.dongnao.module1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.dongnao.base.TestService;
import com.dongnao.router.annotation.Extra;
import com.dongnao.router.annotation.Route;
import com.dongnao.router.core.DNRouter;

/**
 * @author Lance
 * @date 2018/2/23
 */

@Route(path = "/module1/test")
public class Module1Activity extends Activity {

    @Extra
    String msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module1);
        DNRouter.getInstance().inject(this);
        Log.i("module1", "我是模块1:" + msg);


        TestService testService = (TestService) DNRouter.getInstance().build("/main/service1")
                .navigation();
        testService.test();

        TestService testService1 = (TestService) DNRouter.getInstance().build("/main/service2")
                .navigation();
        testService1.test();

        TestService testService2 = (TestService) DNRouter.getInstance().build("/module1/service")
                .navigation();
        testService2.test();

        TestService testService3 = (TestService) DNRouter.getInstance().build("/module2/service")
                .navigation();
        testService3.test();
    }

    public void mainJump(View view) {
        DNRouter.getInstance().build("/main/test").withString("a",
                "从Module1").navigation(this);
    }

    public void module2Jump(View view) {
        DNRouter.getInstance().build("/module2/test").withString("msg",
                "从Module1").navigation(this);
    }
}
