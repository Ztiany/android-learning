package com.imooc.lib_audio.app;

import android.app.Activity;
import android.content.Context;
import com.imooc.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_audio.mediaplayer.core.MusicService;
import com.imooc.lib_audio.mediaplayer.db.GreenDaoHelper;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;
import com.imooc.lib_audio.mediaplayer.view.MusicPlayerActivity;
import java.util.ArrayList;

/**
 * Created by qndroid on 19/5/20.
 *
 * @function 唯一与外界通信的帮助类
 */
public final class AudioHelper {

  //SDK全局Context, 供子模块用
  private static Context mContext;

  public static void init(Context context) {
    mContext = context;
    //初始化本地数据库
    GreenDaoHelper.initDatabase();
  }

  //外部启动MusicService方法
  public static void startMusicService(ArrayList<AudioBean> audios) {
    MusicService.startMusicService(audios);
  }

  public static void addAudio(Activity activity, AudioBean bean) {
    AudioController.getInstance().addAudio(bean);
    MusicPlayerActivity.start(activity);
  }

  public static void pauseAudio() {
    AudioController.getInstance().pause();
  }

  public static void resumeAudio() {
    AudioController.getInstance().resume();
  }

  public static Context getContext() {
    return mContext;
  }
}
