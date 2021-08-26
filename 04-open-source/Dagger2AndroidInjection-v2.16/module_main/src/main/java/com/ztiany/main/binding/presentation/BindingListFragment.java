package com.ztiany.main.binding.presentation;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztiany.main.R;
import com.ztiany.main.binding.data.BindingBean;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-23 15:11
 */
public class BindingListFragment extends Fragment implements Contract.View {

    private RecyclerView mRecyclerView;
    private volatile List<BindingBean> mBindingBeanList;
    private BindingListFragmentCallback mBindingListFragmentCallback;

    @Inject
    Contract.Presenter mBindingListPresenter;
    private View mLayoutView;

    public static Fragment newInstance() {
        return new BindingListFragment();
    }

    @Override
    public void onAttach(Context context) {
        if (getContext() instanceof BindingListFragmentCallback) {
            mBindingListFragmentCallback = (BindingListFragmentCallback) getContext();
        } else {
            throw new IllegalStateException("--- ");
        }
        AndroidSupportInjection.inject(this);
        mBindingListPresenter.setView(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayoutView == null) {
            mLayoutView = inflater.inflate(R.layout.module_main_fragment_binding_list, container, false);
        }
        return mLayoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.module_main_binding_rv_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBindingBeanList == null) {
            mBindingListPresenter.start();
        }
    }


    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                TextView textView = new AppCompatTextView(getContext());
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
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBindingListFragmentCallback.showDetail(mBindingBeanList.get(viewHolder.getAdapterPosition()).getId());
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mBindingBeanList.size();
            }
        });
    }

    @Override
    public void showList(List<BindingBean> bindingBeanList) {
        mBindingBeanList = bindingBeanList;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setupRecyclerView();
            }
        });
    }

    public interface BindingListFragmentCallback {
        void showDetail(String id);
    }
}
