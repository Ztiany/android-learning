package com.ztiany.view.draw;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.BaseViewPagerFragment;
import com.ztiany.view.draw.path.AddPathView;
import com.ztiany.view.draw.path.BezierCircle;
import com.ztiany.view.draw.path.BezierCircleTranslate;
import com.ztiany.view.draw.path.BezierView;
import com.ztiany.view.draw.path.PathEffectView;
import com.ztiany.view.draw.path.PathMeasureCircleSampleBuilder;
import com.ztiany.view.draw.path.PathMeasureLoading;
import com.ztiany.view.draw.path.PathMeasureTanView;
import com.ztiany.view.draw.path.PathMeasureTriangleEffect;
import com.ztiany.view.draw.path.PeachHeartsView;
import com.ztiany.view.draw.path.ThirdBezierView;
import com.ztiany.view.draw.path.TwoBezierView;
import com.ztiany.view.draw.path.WaveView;
import com.ztiany.view.draw.path.WaveView2;

/**
 * author Ztiany                                                                        <br/>
 * email 1169654504@qq.com & ztiany3@gmail.com           <br/>
 * date 2016-04-26 11:37                                                       <br/>
 * description                                                                             <br/>
 * version
 */
public class PathFragment extends BaseViewPagerFragment {

    private String[] titles = {
            "贝塞尔曲线",
            "二阶贝塞尔曲线",
            "三阶贝塞尔曲线",
            "圆圈贝塞尔曲线",
            "圆圈贝塞尔曲线Translate",
            "AddPath",
            "PathEffective",
            "波动1",
            "波动2",
            "PathMeasure Circle",
            "PathMeasure Loading",
            "PathMeasure Triangle",
            "PathMeasure 切线",
            "桃心"
    };

    @Override
    protected PagerAdapter getAdapter() {
        return new BezierAdapter(titles);
    }

    private class BezierAdapter extends BasePagerAdapter {

        BezierAdapter(String[] titles) {
            super(titles);
        }

        @Override
        protected View getItemView(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = new BezierView(getContext());
                    break;
                case 1:
                    view = new TwoBezierView(getContext());
                    break;
                case 2:
                    view = new ThirdBezierView(getContext());
                    break;
                case 3:
                    view = new BezierCircle(getContext());
                    break;
                case 4:
                    view = new BezierCircleTranslate(getContext());
                    break;
                case 5:
                    view = new AddPathView(getContext());
                    break;
                case 6:
                    view = new PathEffectView(getContext());
                    break;
                case 7:
                    view = new WaveView(getContext());
                    break;
                case 8:
                    view = new WaveView2(getContext());
                    break;
                case 9:
                    view = PathMeasureCircleSampleBuilder.create(getContext());
                    break;
                case 10:
                    view = new PathMeasureLoading(getContext());
                    break;
                case 11:
                    view = new PathMeasureTriangleEffect(getContext());
                    break;
                case 12:
                    view = new PathMeasureTanView(getContext());
                    break;
                case 13:
                    view = new PeachHeartsView(getContext());
                    break;
            }
            return view;
        }
    }


}
