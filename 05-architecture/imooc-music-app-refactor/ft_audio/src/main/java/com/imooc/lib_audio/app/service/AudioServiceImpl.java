package com.imooc.lib_audio.app.service;

import android.app.Activity;
import android.content.Context;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.imooc.lib_audio.app.AudioHelper;
import com.imooc.lib_audio.mediaplayer.core.AudioController;
import com.imooc.lib_base.ft_audio.model.CommonAudioBean;
import com.imooc.lib_base.ft_audio.service.AudioService;
import java.util.ArrayList;

/**
 * 音乐播放模块接口实现类
 */
@Route(path = "/audio/audio_service") public class AudioServiceImpl implements AudioService {

  @Override public void pauseAudio() {
    AudioController.getInstance().pause();
  }

  @Override public void resumeAudio() {
    AudioController.getInstance().resume();
  }

  @Override public void addAudio(Activity activity, CommonAudioBean audioBean) {
    AudioHelper.addAudio(activity, audioBean);
  }

  @Override public void startMusicService(ArrayList<CommonAudioBean> audioBeans) {
    AudioHelper.startMusicService(audioBeans);
  }

  @Override public void init(Context context) {

  }
}
