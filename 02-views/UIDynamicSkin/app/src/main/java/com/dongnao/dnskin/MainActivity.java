package com.dongnao.dnskin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dongnao.dnskin.fragment.MusicFragment;
import com.dongnao.dnskin.fragment.RadioFragment;
import com.dongnao.dnskin.fragment.VideoFragment;
import com.dongnao.dnskin.widget.MyTabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * 换肤
 * 颜色: colors.xml 配置需要替换的颜色name 为不同的颜色值
 * 图片： 同上
 * 选择器：同上 (如 颜色选择器，皮肤包中的颜色选择器会使用皮肤包中的颜色)
 * 字体：strings.xml 配置 typeface 路径指向 assets 目录下字体文件
 * 自定义View 需要实现SkinViewSupport接口自行实现换肤逻辑(包括support中的View )
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");

        setContentView(R.layout.activity_main);
        MyTabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);
        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list, listTitle);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        //在 Activity 中有一些特殊的 View，在其创建后（构造函数调用完毕后）会设置再次设置一些属性，
        // 从而导致换肤框架的属性被覆盖，换肤失败，所以遇到这样的场景再应用一次换肤框架设置好的属性。
        //SkinManager.getInstance().updateSkin(this);
    }

    /**
     * 进入换肤
     *
     * @param view
     */
    public void skinSelect(View view) {
        startActivity(new Intent(this, SkinActivity.class));
    }

    public void changeSelf(View view) {
        ((TextView) view).setText("你好啊");
    }

}
