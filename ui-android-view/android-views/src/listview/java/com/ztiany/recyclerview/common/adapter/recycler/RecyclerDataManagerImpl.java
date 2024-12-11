package com.ztiany.recyclerview.common.adapter.recycler;

import com.ztiany.view.utils.Checker;
import com.ztiany.recyclerview.common.adapter.DataManager;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


final class RecyclerDataManagerImpl<T> implements DataManager<T> {

    private List<T> mData;
    private RecyclerView.Adapter mAdapter;
    private HeaderAndFooterSize mHeaderAndFooterSize;

    RecyclerDataManagerImpl(List<T> tList, RecyclerView.Adapter adapter) {
        mData = tList;
        mAdapter = adapter;
    }

    @Override
    public boolean isEmpty() {
        return getDataSize() == 0;
    }

    @Override
    public void add(T element) {
        addAt(getDataSize(), element);
    }

    @Override
    public void addAt(int location, T element) {
        if (mData != null) {
            if (mData.isEmpty()) {
                mData.add(element);
                notifyItemInserted(getHeaderSize());
            } else {
                int size = mData.size();
                int lastIndex = (location >= size ? size : location) + getHeaderSize();
                mData.add(location, element);
                notifyItemInserted(lastIndex);
            }
        } else {
            mData = new ArrayList<>();
            mData.add(element);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItems(List<T> elements) {
        addItemsAt(getDataSize(), elements);
    }

    @Override
    public void addItemsChecked(List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }
        if (mData == null) {
            addItems(elements);
            return;
        }
        boolean hasRemovedElements = false;
        for (T element : elements) {
            if (element == null) {
                continue;
            }
            if (mData.contains(element)) {
                mData.remove(element);
                if (!hasRemovedElements) {
                    hasRemovedElements = true;
                }
            }
        }
        if (hasRemovedElements) {
            mData.addAll(elements);
            notifyDataSetChanged();
        } else {
            addItems(elements);
        }
    }

    @Override
    public void addItemsAt(int location, List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }
        if (this.mData != null) {

            if (mData.isEmpty()) {
                mData.addAll(elements);
                notifyItemRangeInserted(getHeaderSize(), elements.size());
            } else {
                int size = mData.size();
                int lastIndex = (location >= size ? size : location) + getHeaderSize();
                this.mData.addAll(location, elements);
                notifyItemRangeInserted(lastIndex, elements.size());
            }

        } else {
            this.mData = new ArrayList<>(elements.size());
            mData.addAll(elements);
            notifyDataSetChanged();
        }
    }

    @Override
    public void replace(T oldElem, T newElem) {
        if (mData != null && mData.contains(newElem)) {
            replaceAt(mData.indexOf(oldElem), newElem);
        }
    }

    @Override
    public void replaceAt(int index, T element) {
        if (getDataSize() > index) {
            mData.set(index, element);
            notifyItemChanged(index + getHeaderSize());
        }
    }

    @Override
    public void replaceAll(List<T> elements) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        if (elements != null) {
            mData.addAll(elements);
        }
        notifyDataSetChanged();
    }

    @Override
    public void setDataSource(List<T> newDataSource, boolean notifyDataSetChanged) {
        mData = newDataSource;
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(T element) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        if (mData.contains(element)) {
            int index = mData.indexOf(element) + getHeaderSize();
            mData.remove(element);
            notifyItemRemoved(index + getHeaderSize());
        }
    }

    @Override
    public void removeAt(int index) {
        if (getDataSize() > index) {
            mData.remove(index);
            notifyItemRemoved(index + getHeaderSize());
        }
    }

    @Override
    public void removeItems(List<T> elements) {
        if (!Checker.isEmpty(elements) && mData != null && mData.containsAll(elements)) {
            mData.removeAll(elements);
            notifyDataSetChanged();
        }
    }

    @Override
    public T getItem(int position) {
        position = position - getHeaderSize();
        if (position >= 0 && getDataSize() > position) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public final int getDataSize() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean contains(T element) {
        return mData != null && mData.contains(element);
    }

    @Override
    public void clear() {
        if (mData != null) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemPosition(T t) {
        List<T> items = getItems();
        if (items == null) {
            return -1;
        }
        return items.indexOf(t);
    }

    @Override
    public List<T> getItems() {
        return mData;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Size
    ///////////////////////////////////////////////////////////////////////////
    private int getHeaderSize() {
        return mHeaderAndFooterSize == null ? 0 : mHeaderAndFooterSize.getHeaderSize();
    }

    ///////////////////////////////////////////////////////////////////////////
    // Adapter Call
    ///////////////////////////////////////////////////////////////////////////
    private void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    private void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }

    private void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
    }

    private void notifyItemRangeInserted(int position, int size) {
        mAdapter.notifyItemRangeInserted(position, size);
    }

    private void notifyItemRemoved(int index) {
        mAdapter.notifyItemRemoved(index);
    }

    void setHeaderAndFooterSize(HeaderAndFooterSize headerAndFooterSize) {
        mHeaderAndFooterSize = headerAndFooterSize;
    }
}
