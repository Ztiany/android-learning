package com.ztiany.recyclerview.adapter_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ztiany.view.R;
import com.ztiany.recyclerview.adapter.list.BaseListAdapter;
import com.ztiany.recyclerview.adapter.list.SmartViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author Ztiany
 * Date : 2018-08-14 16:32
 */
public class ListViewFragment extends BaseListFragment {

    private Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_adapter_fragment_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = view.findViewById(R.id.list_view);
        mAdapter = new ListViewFragment.Adapter(getContext());
        mAdapter.replaceAll(DataSource.crateList());
        listView.setAdapter(mAdapter);
    }

    @Override
    protected void modifyFirst() {
        Person item = mAdapter.getItem(0);
        item.setName("大帅哥");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void removeFirst() {
        mAdapter.removeAt(0);
    }

    @Override
    protected void addFirst() {
        Person person = new Person(100, "王思聪", "北京");
        mAdapter.addAt(0, person);
    }

    @Override
    protected void addAll() {
        List<Person> list = new ArrayList<>();
        list.add(new Person(12, "王思聪", "北京三里屯"));
        list.add(new Person(13, "王聪思", "北京公主坟"));
        list.add(new Person(14, "思王聪", "北京故宫"));
        list.add(new Person(15, "思聪王", "北京中南海"));
        mAdapter.addItems(list);
    }

    @Override
    protected void addOne() {
        Person person = new Person(28, "张三", "湖南");
        mAdapter.add(person);
    }

    private class Adapter extends BaseListAdapter<Person, SmartViewHolder> {

        Adapter(Context context) {
            super(context);
        }

        @Override
        @SuppressLint("SetTextI18n")
        protected void onBindData(SmartViewHolder viewHolder, Person item) {
            TextView nameTv = viewHolder.helper().getView(R.id.nameTv);
            TextView addressTv = viewHolder.helper().getView(R.id.addressTv);
            nameTv.setText("姓名：" + item.getName() + " ID: " + item.getId());
            addressTv.setText(item.getAddress());
        }

        @Override
        protected SmartViewHolder onCreateViewHolder(LayoutInflater layoutInflater, ViewGroup parent, int type) {
            return new SmartViewHolder(layoutInflater.inflate(R.layout.rv_adapter_item, parent, false));
        }

    }

}