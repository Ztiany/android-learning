package com.imooc.imooc_voice.view.discory;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imooc.imooc_voice.R;
import com.imooc.imooc_voice.model.discory.RecommandHeadValue;
import com.imooc.imooc_voice.model.discory.RecommandMiddleValue;
import com.imooc.lib_commin_ui.recyclerview.CommonAdapter;
import com.imooc.lib_commin_ui.recyclerview.base.ViewHolder;
import com.imooc.lib_image_loader.app.ImageLoaderManager;

public class DiscoryRecommandView extends RelativeLayout {
    private Context mContext;

    /*
     * UI
     */
    private RecyclerView mRecyclerView;
    /*
     * Data
     */
    private RecommandHeadValue mHeaderValue;

    public DiscoryRecommandView(Context context, RecommandHeadValue recommandHeadValue) {
        this(context, null, recommandHeadValue);
    }

    public DiscoryRecommandView(Context context, AttributeSet attrs,
                                RecommandHeadValue recommandHeadValue) {
        super(context, attrs);
        mContext = context;
        mHeaderValue = recommandHeadValue;
        initView();
    }

    private void initView() {
        View rootView =
                LayoutInflater.from(mContext).inflate(R.layout.item_discory_head_recommand_layout, this);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mRecyclerView.setAdapter(new CommonAdapter<RecommandMiddleValue>(mContext,
                R.layout.item_discory_head_recommand_recycler_layout, mHeaderValue.middle) {
            @Override
            protected void convert(ViewHolder holder, RecommandMiddleValue value, int position) {
                holder.setText(R.id.text_view, value.info);
                ImageView imageView = holder.getView(R.id.image_view);
                ImageLoaderManager.getInstance().displayImageForView(imageView, value.imageUrl);
            }
        });
    }
}
