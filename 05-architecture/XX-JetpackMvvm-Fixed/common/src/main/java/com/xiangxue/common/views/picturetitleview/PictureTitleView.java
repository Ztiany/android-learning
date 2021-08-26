package com.xiangxue.common.views.picturetitleview;

import android.content.Context;
import android.view.View;

import com.xiangxue.base.customview.BaseCustomView;
import com.xiangxue.common.R;
import com.xiangxue.common.databinding.PictureTitleViewBinding;
import com.xiangxue.webview.WebviewActivity;

public class PictureTitleView extends BaseCustomView<PictureTitleViewBinding, PictureTitleViewModel> {
    public PictureTitleView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.picture_title_view;
    }

    @Override
    public void onRootClicked(View view) {
        WebviewActivity.startCommonWeb(getContext(), "News", data.jumpUri);
    }

    @Override
    protected void setDataToView(PictureTitleViewModel pictureTitleViewModel) {
        binding.setViewModel(pictureTitleViewModel);
    }
}
