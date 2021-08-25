package com.xiangxue.news.homefragment.newslist;

import com.xiangxue.base.customview.BaseCustomViewModel;
import com.xiangxue.base.mvvm.model.BaseMvvmModel;
import com.xiangxue.common.views.picturetitleview.PictureTitleViewModel;
import com.xiangxue.common.views.titleview.TitleViewModel;
import com.xiangxue.network.TecentNetworkApi;
import com.xiangxue.network.observer.BaseObserver;
import com.xiangxue.news.homefragment.api.NewsApiInterface;
import com.xiangxue.news.homefragment.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel extends BaseMvvmModel<NewsListBean, List<BaseCustomViewModel>> {
    private String mChannelId;
    private String mChannelName;

    public NewsListModel(String channelId, String channelName) {
        super(true, channelId + channelName + "_preference_key", null,1);
        mChannelId = channelId;
        mChannelName = channelName;
    }

    @Override
    public void load() {
        TecentNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId,
                        mChannelName, String.valueOf(mPage))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this, this)));
    }

    @Override
    public void onSuccess(NewsListBean newsListBean, boolean isFromCache) {
        List<BaseCustomViewModel> viewModels = new ArrayList<>();
        for (NewsListBean.Contentlist contentlist : newsListBean.showapiResBody.pagebean.contentlist) {
            if (contentlist.imageurls != null && contentlist.imageurls.size() > 0) {
                PictureTitleViewModel pictureTitleViewModel = new PictureTitleViewModel();
                pictureTitleViewModel.pictureUrl = contentlist.imageurls.get(0).url;
                pictureTitleViewModel.jumpUri = contentlist.link;
                pictureTitleViewModel.title = contentlist.title;
                viewModels.add(pictureTitleViewModel);
            } else {
                TitleViewModel titleViewModel = new TitleViewModel();
                titleViewModel.jumpUri = contentlist.link;
                titleViewModel.title = contentlist.title;
                viewModels.add(titleViewModel);
            }
        }
        notifyResultToListener(newsListBean, viewModels, isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {
        loadFail(e.getMessage());
    }
}
