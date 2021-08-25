package com.imooc.ft_home.model.discory;

import com.imooc.lib_base.BaseModel;
import java.util.ArrayList;

/**
 * @author: vision
 * @function:
 */
public class RecommandHeadValue extends BaseModel {

  public ArrayList<String> ads;
  public ArrayList<RecommandMiddleValue> middle;
  public ArrayList<RecommandFooterValue> footer;
}
