package com.ztiany.view.draw;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.BaseViewPagerFragment;
import com.ztiany.view.R;
import com.ztiany.view.draw.matrix.CanvasMatrix1View;
import com.ztiany.view.draw.matrix.CanvasMatrix2View;
import com.ztiany.view.draw.matrix.CanvasMatrix3View;
import com.ztiany.view.draw.matrix.CanvasSetMatrixDemoView;
import com.ztiany.view.draw.matrix.MatrixDemo1View;
import com.ztiany.view.draw.matrix.MatrixView;
import com.ztiany.view.draw.matrix.PolyToPolyDemoView;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-06 23:49
 */
public class MatrixFragment extends BaseViewPagerFragment {

    private String[] titles = {
            "Matrix 变换循序",
            "Canvas Matrix1",
            "Canvas Matrix2",
            "Canvas Matrix3",
            "canvas setMatrix",
            "Matrix Image View",
            "PolyToPoly"
    };

    @Override
    protected PagerAdapter getAdapter() {
        return new MatrixAdapter(titles);
    }


    private class MatrixAdapter extends BasePagerAdapter {

        MatrixAdapter(String[] titles) {
            super(titles);
        }

        @Override
        protected View getItemView(ViewGroup container, int position) {
            View view = null;
            switch (position) {
                case 0:
                    view = new MatrixDemo1View(getContext());
                    break;
                case 1:
                    view = new CanvasMatrix1View(getContext());
                    break;
                case 2:
                    view = new CanvasMatrix2View(getContext());
                    break;
                case 3:
                    view = new CanvasMatrix3View(getContext());
                    break;
                case 4:
                    view = new CanvasSetMatrixDemoView(getContext());
                    break;
                case 5:
                    MatrixView matrixView = new MatrixView(getContext());
                    matrixView.setImageResource(R.drawable.img_girl_01);
                    view = matrixView;
                    break;
                case 6:
                    view = new PolyToPolyDemoView(getContext());
            }
            return view;
        }
    }
}