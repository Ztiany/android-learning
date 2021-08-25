package com.ztiany.recyclerview.adapter_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztiany.view.utils.Checker;
import com.ztiany.view.R;
import com.ztiany.recyclerview.adapter.recycler.DiffRecyclerAdapter;
import com.ztiany.recyclerview.adapter.recycler.SmartViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Ztiany
 * Date : 2018-08-14 16:31
 */
public class RecyclerViewFragment extends BaseListFragment {

    private Adapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_adapter_fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new Adapter(requireContext(), itemCallback);
        mAdapter.replaceAll(DataSource.crateList());
        recyclerView.setAdapter(mAdapter);
    }

    private DiffUtil.ItemCallback<Person> itemCallback = new DiffUtil.ItemCallback<Person>() {
        @Override
        public boolean areItemsTheSame(Person oldItem, Person newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Person oldItem, Person newItem) {
            return Checker.equals(oldItem.getAddress(), newItem.getAddress()) && Checker.equals(oldItem.getName(), newItem.getName());
        }
    };

    @Override
    protected void modifyFirst() {
        Person item = mAdapter.getItem(0);
        item.setName("湛大帅");
        mAdapter.notifyItemChanged(0);
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

    private class Adapter extends DiffRecyclerAdapter<Person, SmartViewHolder> {

        Adapter(@NonNull Context context, @NonNull DiffUtil.ItemCallback<Person> itemCallback) {
            super(context, itemCallback);
        }

        @NonNull
        @Override
        public SmartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SmartViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_adapter_item, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull SmartViewHolder viewHolder, int position) {
            Person item = getItem(position);
            TextView nameTv = viewHolder.helper().getView(R.id.nameTv);
            TextView addressTv = viewHolder.helper().getView(R.id.addressTv);
            nameTv.setText("姓名：" + item.getName() + " ID: " + item.getId());
            addressTv.setText(item.getAddress());
        }
    }

}
