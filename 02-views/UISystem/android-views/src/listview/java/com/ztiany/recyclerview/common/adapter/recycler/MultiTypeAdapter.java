package com.ztiany.recyclerview.common.adapter.recycler;

import android.content.Context;

import com.ztiany.recyclerview.common.adapter.DataManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;


public class MultiTypeAdapter extends com.drakeet.multitype.MultiTypeAdapter implements DataManager<Object> {

    protected final Context mContext;

    private RecyclerDataManagerImpl<Object> mRecyclerDataManager;

    public MultiTypeAdapter(Context context) {
        super();
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>();
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items) {
        super();
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>(items);
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    public MultiTypeAdapter(Context context, @NonNull List<?> items, int initialCapacity) {
        super(items, initialCapacity);
        mContext = context;
        ArrayList<Object> objects = new ArrayList<>(items);
        mRecyclerDataManager = new RecyclerDataManagerImpl<>(objects, this);
        super.setItems(objects);
    }

    @Override
    public void add(Object elem) {
        mRecyclerDataManager.add(elem);
    }

    @Override
    public void addAt(int location, Object elem) {
        mRecyclerDataManager.addAt(location, elem);
    }

    @Override
    public void addItems(List<Object> elements) {
        mRecyclerDataManager.addItems(elements);
    }

    @Override
    public void addItemsChecked(List<Object> elements) {
        mRecyclerDataManager.addItemsChecked(elements);
    }

    @Override
    public void addItemsAt(int location, List<Object> elements) {
        mRecyclerDataManager.addItemsAt(location, elements);
    }

    @Override
    public void replace(Object oldElem, Object newElem) {
        mRecyclerDataManager.replace(oldElem, newElem);
    }

    @Override
    public void replaceAt(int index, Object elem) {
        mRecyclerDataManager.replaceAt(index, elem);
    }

    @Override
    public void replaceAll(List<Object> elements) {
        mRecyclerDataManager.replaceAll(elements);
    }

    @Override
    public void setDataSource(List<Object> newDataSource, boolean notifyDataSetChanged) {
        super.setItems(newDataSource);
        mRecyclerDataManager.setDataSource(newDataSource, notifyDataSetChanged);
    }

    @Override
    public void remove(Object elem) {
        mRecyclerDataManager.remove(elem);
    }

    @Override
    public void removeAt(int index) {
        mRecyclerDataManager.removeAt(index);
    }

    @Override
    public void removeItems(List<Object> elements) {
        mRecyclerDataManager.removeItems(elements);
    }

    @Override
    public Object getItem(int position) {
        return mRecyclerDataManager.getItem(position);
    }

    @Override
    public int getDataSize() {
        return mRecyclerDataManager.getDataSize();
    }

    @Override
    public boolean contains(Object elem) {
        return mRecyclerDataManager.contains(elem);
    }

    @Override
    public boolean isEmpty() {
        return mRecyclerDataManager.isEmpty();
    }

    @Override
    public void clear() {
        mRecyclerDataManager.clear();
    }

    @NonNull
    @Override
    public List<Object> getItems() {
        return mRecyclerDataManager.getItems();
    }

    @Override
    public int getItemPosition(Object o) {
        return mRecyclerDataManager.getItemPosition(o);
    }

    @Override
    public void setItems(@NonNull List<?> items) {
        ArrayList<Object> objects = new ArrayList<>(items);
        super.setItems(objects);
        mRecyclerDataManager.setDataSource(objects, true);
    }

}