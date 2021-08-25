package com.ztiany.view.scroll;

import com.ztiany.view.BaseParentFragment;
import com.ztiany.view.BaseViewFragment;
import com.ztiany.view.R;

import java.util.List;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-05 16:09
 */
public class ScrollFragment extends BaseParentFragment {

    @Override
    protected void onFillChildrenUp(List<FragInfo> fragInfoList) {
        fragInfoList.add(new FragInfo("嵌套滑动1", BaseViewFragment.newInstance(R.layout.scroll_nested_sample1)));
        fragInfoList.add(new FragInfo("嵌套滑动2", BaseViewFragment.newInstance(R.layout.scroll_nested_sample2)));
        fragInfoList.add(new FragInfo("Over Scroller", BaseViewFragment.newInstance(R.layout.scroll_over_scroller)));
        fragInfoList.add(new FragInfo("多指处理", BaseViewFragment.newInstance(R.layout.scroll_multi_drag)));
    }

}
