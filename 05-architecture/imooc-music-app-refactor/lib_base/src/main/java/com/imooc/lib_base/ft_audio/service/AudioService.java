package com.imooc.lib_base.ft_audio.service;

import android.app.Activity;
import com.alibaba.android.arouter.facade.template.IProvider;
import com.imooc.lib_base.ft_audio.model.CommonAudioBean;
import java.util.ArrayList;

/**
 * 音乐模块对外接口
 */
public interface AudioService extends IProvider {

  void pauseAudio();

  void resumeAudio();

  void addAudio(Activity activity, CommonAudioBean audioBean);

  void startMusicService(ArrayList<CommonAudioBean> audioBeans);
}
