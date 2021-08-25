package com.ztiany.recyclerview.adapter.recycler;

import  androidx.recyclerview.widget.RecyclerView;

/**
 * @author Ztiany
 *         Email: 1169654504@qq.com
 *         Date : 2017-09-13 15:33
 */
public abstract class ItemViewBinder<T, VH extends RecyclerView.ViewHolder> extends com.drakeet.multitype.ItemViewBinder<T, VH> {

    @SuppressWarnings("unchecked")
    protected final MultiTypeAdapter getDataManager() {
        return (MultiTypeAdapter) getAdapter();
    }

}
