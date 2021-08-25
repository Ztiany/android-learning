package com.ztiany.recyclerview.diff_util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 参考：https://blog.csdn.net/zxt0601/article/details/52562770
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2018-01-03 16:21
 */
public class AsyncListDifferFragment extends Fragment {

    private AsyncListDifferDataAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_fragment_diff_utils, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new AsyncListDifferDataAdapter();
        recyclerView.setAdapter(mAdapter);
        mAdapter.submitList(DataSource.getDataSource(getContext()));

        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAdd();
            }
        });

        view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDelete();
            }
        });

        view.findViewById(R.id.btn_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doChange();
            }
        });


    }


    private void doAdd() {
        //step 1  俩个数据集合
        final List<TestBean> testBeans = mAdapter.getTestBeans();
        final List<TestBean> newBeans = new ArrayList<>(testBeans);

        TestBean element = new TestBean();

        element.setId((newBeans.size() + 1));

        element.setDes(DataSource.randomDes());

        if (DataSource.randomBoolean()) {
            element.setDrawableId(DataSource.randomDrawable(getContext()));
        }

        newBeans.add(1, element);

        mAdapter.submitList(newBeans);
    }


    private void doChange() {
        final List<TestBean> testBeans = mAdapter.getTestBeans();
        final List<TestBean> newBeans = new ArrayList<>(testBeans);

        int index = DataSource.randomInt(3);
        TestBean oldBean = testBeans.get(index);
        TestBean newBean = new TestBean();
        newBean.setId(oldBean.getId());

        newBean.setDes(DataSource.randomDes());

        if (DataSource.randomBoolean()) {
            newBean.setDrawableId(0);
        } else {
            newBean.setDrawableId(DataSource.randomDrawable(getContext()));
        }
        newBeans.set(index, newBean);

        mAdapter.submitList(newBeans);
    }

    private void doDelete() {
        final List<TestBean> testBeans = mAdapter.getTestBeans();
        final List<TestBean> newBeans = new ArrayList<>(testBeans);

        newBeans.remove(DataSource.randomInt(3));

        mAdapter.submitList(newBeans);
    }

}
