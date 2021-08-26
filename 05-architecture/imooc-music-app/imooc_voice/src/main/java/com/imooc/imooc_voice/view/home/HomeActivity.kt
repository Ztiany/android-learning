package com.imooc.imooc_voice.view.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alibaba.android.arouter.launcher.ARouter
import com.imooc.imooc_voice.R
import com.imooc.imooc_voice.constant.Constant
import com.imooc.imooc_voice.model.KotCHANNEL
import com.imooc.imooc_voice.model.login.LoginEvent
import com.imooc.imooc_voice.utils.UserManager
import com.imooc.imooc_voice.utils.Utils
import com.imooc.imooc_voice.view.home.adpater.HomePagerAdapter
import com.imooc.imooc_voice.view.login.LoginActivity
import com.imooc.lib_audio.app.AudioHelper
import com.imooc.lib_audio.mediaplayer.model.AudioBean
import com.imooc.lib_commin_ui.base.BaseActivity
import com.imooc.lib_commin_ui.pager_indictor.ScaleTransitionPagerTitleView
import com.imooc.lib_image_loader.app.ImageLoaderManager
import com.imooc.lib_update.app.UpdateHelper
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

class HomeActivity : BaseActivity, View.OnClickListener {
    private val CHANNELS = arrayOf(KotCHANNEL.MY, KotCHANNEL.DISCORY, KotCHANNEL.FRIEND)

    private var mAdapter: HomePagerAdapter? = null
    private val mReceiver: KotUpdateReceiver = KotUpdateReceiver()
    /*
     * data
     */
    private val mLists = ArrayList<AudioBean>()

    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBroadcastReceiver()
        EventBus.getDefault().register(this)
        setContentView(R.layout.activity_main)
        initData()
        initView()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.exit_layout -> {
                finish()
                System.exit(0)
            }

            R.id.unloggin_layout -> {
                if (!UserManager.hasLogined) {
                    LoginActivity.start(this)
                } else {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                }
            }

            R.id.toggle_view -> {
                if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                    drawer_layout.closeDrawer(Gravity.LEFT)
                } else {
                    drawer_layout.openDrawer(Gravity.LEFT)
                }
            }

            R.id.home_qrcode -> {
                if (hasPermission(*Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doCameraPermission()
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, *Constant.HARDWEAR_CAMERA_PERMISSION)
                }
            }

            R.id.home_music -> {
                goToMusic()
            }

            R.id.online_music_view -> {
                gotoWebView("https://www.imooc.com")
            }

            R.id.check_update_view -> {
                checkUpdate()
            }
        }
    }

    override fun doCameraPermission() {
        ARouter.getInstance().build(Constant.Router.ROUTER_CAPTURE_ACTIVIYT).navigation()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //退出不销毁task中activity
            moveTaskToBack(true)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        unRegisterBroadcastReceiver()
    }

    private fun initView() {
        toggle_view.setOnClickListener(this)
        search_view.setOnClickListener(this)
        //初始化adpater
        mAdapter = HomePagerAdapter(supportFragmentManager, CHANNELS)
        view_pager.adapter = mAdapter
        initMagicIndicator()
        home_qrcode.setOnClickListener(this)
        home_music.setOnClickListener(this)
        online_music_view.setOnClickListener(this)
        check_update_view.setOnClickListener(this)
        unloggin_layout.setOnClickListener(this)
        exit_layout.setOnClickListener(this)
    }

    private fun initMagicIndicator() {
        magic_indicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.isAdjustMode = true
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return CHANNELS.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.run {
                    text = CHANNELS[index].key
                    textSize = 19f
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    normalColor = Color.parseColor("#999999")
                    selectedColor = Color.parseColor("#333333")
                    setOnClickListener { view_pager.currentItem = index }
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }

            override fun getTitleWeight(context: Context?, index: Int): Float {
                return 1.0f
            }
        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

    private fun initData() {
        mLists.run {
            add(AudioBean("100001", "http://sp-sycdn.kuwo.cn/resource/n2/85/58/433900159.mp3",
                    "以你的名字喊我", "周杰伦", "七里香", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698076304&di=e6e99aa943b72ef57b97f0be3e0d2446&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fblog%2F201401%2F04%2F20140104170315_XdG38.jpeg",
                    "4:30"))
            add(
                    AudioBean("100002", "http://sq-sycdn.kuwo.cn/resource/n1/98/51/3777061809.mp3", "勇气",
                            "梁静茹", "勇气", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698193627&di=711751f16fefddbf4cbf71da7d8e6d66&imgtype=jpg&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D213168965%2C1040740194%26fm%3D214%26gp%3D0.jpg",
                            "4:40"))
            add(
                    AudioBean("100003", "http://sp-sycdn.kuwo.cn/resource/n2/52/80/2933081485.mp3", "灿烂如你",
                            "汪峰", "春天里", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698239736&di=3433a1d95c589e31a36dd7b4c176d13a&imgtype=0&src=http%3A%2F%2Fpic.zdface.com%2Fupload%2F201051814737725.jpg",
                            "3:20"))
            add(
                    AudioBean("100004", "http://sr-sycdn.kuwo.cn/resource/n2/33/25/2629654819.mp3", "小情歌",
                            "五月天", "小幸运", "电影《不能说的秘密》主题曲,尤其以最美的不是下雨天,是与你一起躲过雨的屋檐最为经典",
                            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1559698289780&di=5146d48002250bf38acfb4c9b4bb6e4e&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20131220%2Fbki-20131220170401-1254350944.jpg",
                            "2:45"))

        }
        AudioHelper.startMusicService(mLists)
    }

    private fun goToMusic() {
        ARouter.getInstance().build(Constant.Router.ROUTER_MUSIC_ACTIVIYT).navigation()
    }

    private fun gotoWebView(url: String) {
        ARouter.getInstance()
                .build(Constant.Router.ROUTER_WEB_ACTIVIYT)
                .withString("url", url)
                .navigation()
    }

    //启动检查更新
    private fun checkUpdate() {
        UpdateHelper.checkUpdate(this)
    }

    private fun registerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mReceiver, IntentFilter(UpdateHelper.UPDATE_ACTION))
    }

    private fun unRegisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver)
    }

    /**
     * 处理登陆事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        unloggin_layout.visibility = View.GONE
        avatr_view.visibility = View.VISIBLE
        ImageLoaderManager.getInstance()
                .displayImageForCircle(avatr_view, UserManager.mUser?.data?.photoUrl)
    }

    /**
     * 接收Update发送的广播
     */
    inner class KotUpdateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //启动安装页面
            context.startActivity(
                    Utils.getInstallApkIntent(context, intent.getStringExtra(UpdateHelper.UPDATE_FILE_KEY)))
        }
    }
}