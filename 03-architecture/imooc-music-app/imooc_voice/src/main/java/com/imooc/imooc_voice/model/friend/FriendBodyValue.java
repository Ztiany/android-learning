package com.imooc.imooc_voice.model.friend;

import com.imooc.imooc_voice.model.BaseModel;
import com.imooc.lib_audio.mediaplayer.model.AudioBean;
import java.util.ArrayList;

/**
 * @文件描述：朋友实体
 */
public class FriendBodyValue extends BaseModel {

  public int type;
  public String avatr;
  public String name;
  public String fans;
  public String text;
  public ArrayList<String> pics;
  public String videoUrl;
  public String zan;
  public String msg;
  public AudioBean audioBean;
}
