package com.xiangxue.common.views.titleview;

import android.content.Context;
import android.view.View;

import com.xiangxue.base.customview.BaseCustomView;
import com.xiangxue.common.R;
import com.xiangxue.common.databinding.TitleViewBinding;
import com.xiangxue.webview.WebviewActivity;

public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.title_view;
    }

    @Override
    public void onRootClicked(View view) {
        WebviewActivity.startCommonWeb(getContext(), "News", data.jumpUri);
    }

    @Override
    protected void setDataToView(TitleViewModel titleViewModel) {
        binding.setViewModel(titleViewModel);
    }
}
