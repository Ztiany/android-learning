package com.maniu.mn_vip_upload_point;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.maniu.mn_vip_upload_point.annotation.MaiDianData;
import com.maniu.mn_vip_upload_point.annotation.MaiDianData2;
import com.maniu.mn_vip_upload_point.annotation.PermasAnnotation;

public class MainActivity extends AppCompatActivity {

    private long times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        times = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 给服务器上传数据
     */
    public void sendData(View view) {
        //从服务器获取到User对象
        setUserToUi("Zee", "123456");
        setUserToUi("David", "456789", 18);
        getData2("xxx异常", "202012121");
    }

    //手机用户的使用使用时长的数据
    @MaiDianData
    public void setUserToUi(
            @PermasAnnotation("name") String name,
            @PermasAnnotation("password") String password
    ) {
        //做更新UI的操作
    }

    //手机用户的使用使用时长的数据
    @MaiDianData
    public void setUserToUi(
            @PermasAnnotation("name") String name,
            @PermasAnnotation("password") String password,
            @PermasAnnotation("age") int age
    ) {

    }

    //上传异常统计
    @MaiDianData2
    public void getData2(
            @PermasAnnotation("ExceptionContent") String content,
            @PermasAnnotation("ExceptionTime") String time
    ) {

    }

}
