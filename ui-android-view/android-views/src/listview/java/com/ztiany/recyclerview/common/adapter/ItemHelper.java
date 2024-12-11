package com.ztiany.recyclerview.common.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;


/**
 * @author Ztiany
 *         Email ：1169654504@qq.com
 *         Date ：015-12-29 20:47
 */
public class ItemHelper {

    private View mItemView;
    private SparseArray<View> views;

    public ItemHelper(View itemView) {
        mItemView = itemView;
        views = new SparseArray<>();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public ItemHelper setText(CharSequence str, @IdRes int viewId) {
        ((TextView) getView(viewId)).setText(str == null ? "" : str);
        return this;
    }

    public ItemHelper setText(@StringRes int strId, @IdRes int viewId) {
        ((TextView) getView(viewId)).setText(strId);
        return this;
    }

    public ItemHelper setTag(@NonNull Object object, @IdRes int tagId, @IdRes int viewID) {
        View view = getView(viewID);
        view.setTag(tagId, object);
        return this;
    }

    public ItemHelper setTag(@NonNull Object object, @IdRes int viewID) {
        View view = getView(viewID);
        view.setTag(object);
        return this;
    }

    public <T> T getTag(@IdRes int tagId, @IdRes int viewID) {
        View view = getView(viewID);
        @SuppressWarnings("unchecked")
        T tag = (T) view.getTag(tagId);
        return tag;
    }

    public <T> T getTag(@IdRes int viewID) {
        View view = getView(viewID);
        @SuppressWarnings("unchecked")
        T tag = (T) view.getTag();
        return tag;
    }

}