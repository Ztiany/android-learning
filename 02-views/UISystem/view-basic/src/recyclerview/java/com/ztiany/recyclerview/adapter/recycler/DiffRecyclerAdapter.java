package com.ztiany.recyclerview.adapter.recycler;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;

import com.ztiany.view.utils.Checker;
import com.ztiany.recyclerview.adapter.DataManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView 的适配器
 * 注意：  只有setDataSource才能替换原有数据源的引用。
 *
 * @param <T> 当前列表使用的数据类型
 * @author Ztiany
 * date :    2015-05-11 22:38
 * email:    1169654504@qq.com
 */
public abstract class DiffRecyclerAdapter<T, VH extends ViewHolder> extends RecyclerView.Adapter<VH> implements DataManager<T> {

    @NonNull
    protected Context mContext;

    private AsyncListDiffer<T> mAsyncListDiffer;

    public DiffRecyclerAdapter(@NonNull Context context, @NonNull DiffUtil.ItemCallback<T> itemCallback) {
        this.mContext = context;

        AsyncDifferConfig<T> differConfig = new AsyncDifferConfig.Builder<>(itemCallback)
                .setBackgroundThreadExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();

        mAsyncListDiffer = new AsyncListDiffer<>(new AdapterListUpdateCallback(this), differConfig);
    }

    @Override
    public int getItemCount() {
        return getDataSize();
    }

    @NonNull
    @Override
    public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public abstract void onBindViewHolder(@NonNull VH viewHolder, int position);

    ///////////////////////////////////////////////////////////////////////////
    // DataManager
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void add(T element) {
        List<T> newList = newList();
        newList.add(element);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void addAt(int location, T element) {
        if (location > getDataSize()) {
            location = getDataSize();
        }
        List<T> newList = newList();
        newList.add(location, element);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void addItems(List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }
        List<T> newList = newList();
        newList.addAll(elements);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void addItemsChecked(List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }

        List<T> newList = newList();

        for (T element : elements) {
            if (element == null) {
                continue;
            }
            newList.remove(element);
        }

        newList.addAll(elements);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void addItemsAt(int location, List<T> elements) {
        if (Checker.isEmpty(elements)) {
            return;
        }
        List<T> newList = newList();

        if (location > newList.size()) {
            location = newList.size();
        }

        newList.addAll(location, elements);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void replace(T oldElement, T newElement) {
        if (!contains(oldElement)) {
            return;
        }
        List<T> newList = newList();
        newList.set(newList.indexOf(oldElement), newElement);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void replaceAt(int index, T element) {
        if (getDataSize() > index) {
            List<T> newList = newList();
            newList.set(index, element);
            mAsyncListDiffer.submitList(newList);
        }
    }

    @Override
    public void replaceAll(List<T> elements) {
        List<T> newList = new ArrayList<>(elements);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void remove(T element) {
        if (contains(element)) {
            List<T> newList = newList();
            newList.remove(element);
            mAsyncListDiffer.submitList(newList);
        }
    }

    @Override
    public void removeItems(List<T> elements) {
        if (Checker.isEmpty(elements) || isEmpty() || !getItems().containsAll(elements)) {
            return;
        }
        List<T> newList = newList();
        newList.removeAll(elements);
        mAsyncListDiffer.submitList(newList);
    }

    @Override
    public void removeAt(int index) {
        if (getDataSize() > index) {
            List<T> newList = newList();
            newList.remove(index);
            mAsyncListDiffer.submitList(newList);
        }
    }

    @Override
    public T getItem(int position) {
        return getDataSize() > position ? getItems().get(position) : null;
    }

    @Override
    public final int getDataSize() {
        return getItems() == null ? 0 : getItems().size();
    }

    @Override
    public boolean contains(T element) {
        return !isEmpty() && getItems().contains(element);
    }

    @Override
    public void clear() {
        mAsyncListDiffer.submitList(null);
    }

    @Override
    public void setDataSource(List<T> elements, boolean notifyDataSetChanged) {
        mAsyncListDiffer.submitList(elements);
    }

    @Override
    public List<T> getItems() {
        return mAsyncListDiffer.getCurrentList();
    }

    @Override
    public boolean isEmpty() {
        return getDataSize() == 0;
    }

    @Override
    public int getItemPosition(T t) {
        return isEmpty() ? -1 : getItems().indexOf(t);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Utils
    ///////////////////////////////////////////////////////////////////////////

    private List<T> newList() {
        if (getItems() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(getItems());
        }
    }

}

