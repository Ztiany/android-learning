package com.xiangxue.news.homefragment.headlinenews;

import com.xiangxue.base.mvvm.viewmodel.BaseMvvmViewModel;
import com.xiangxue.news.homefragment.api.NewsChannelsBean;

public class HeadlineNewsViewModel extends BaseMvvmViewModel<NewsChannelModel, NewsChannelsBean.ChannelList> {

    @Override
    public NewsChannelModel createModel() {
        return new NewsChannelModel();
    }
}
