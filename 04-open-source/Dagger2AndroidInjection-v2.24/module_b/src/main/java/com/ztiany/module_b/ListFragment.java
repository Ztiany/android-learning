package com.ztiany.module_b;

import android.content.Context;
import android.databinding.Observable;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 15:35
 */
public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ListAdapter mListAdapter;

    @Inject
    ListViewModel mListViewModel;

    public static Fragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mRecyclerView = new RecyclerView(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mListAdapter = new ListAdapter();
        mRecyclerView.setAdapter(mListAdapter);
        mListViewModel.mListObservableField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mListAdapter.setBindingBeanList(mListViewModel.mListObservableField.get());
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListViewModel.start();
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Bean> mBindingBeanList;

        void setBindingBeanList(List<Bean> bindingBeanList) {
            mBindingBeanList = bindingBeanList;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            TextView textView = new ItemTextView(getContext());
            textView.setPadding(30, 30, 30, 30);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundColor(Color.RED);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return new RecyclerView.ViewHolder(textView) {

            };
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
            ((TextView) viewHolder.itemView).setText(String.format("User id = %s", mBindingBeanList.get(i).getId()));
        }

        @Override
        public int getItemCount() {
            return mBindingBeanList == null ? 0 : mBindingBeanList.size();
        }
    }
}
