package com.ztiany.recyclerview.diff_util;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
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
public class DiffUtilFragment extends Fragment {

    private DataAdapter mAdapter;
    private HandlerThread mHandlerThread = new HandlerThread("diff");
    private Handler mHandler;
    private Handler mUIHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        mUIHandler = new Handler();
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
        mAdapter = new DataAdapter(DataSource.getDataSource(getContext()));
        recyclerView.setAdapter(mAdapter);

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

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                //step 2 通过DiffCallback就按差异
                //第二个参数代表是否检测Item的移动，改为false算法效率更高，按需设置，我们这里是true。
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(testBeans, newBeans), true);

                dispatchToAdapter(diffResult, newBeans);
            }
        });

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

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(testBeans, newBeans));
                dispatchToAdapter(diffResult, newBeans);
            }
        });
    }

    private void doDelete() {
        final List<TestBean> testBeans = mAdapter.getTestBeans();
        final List<TestBean> newBeans = new ArrayList<>(testBeans);

        newBeans.remove(DataSource.randomInt(3));

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(testBeans, newBeans));
                dispatchToAdapter(diffResult, newBeans);
            }
        });
    }

    private void dispatchToAdapter(final DiffUtil.DiffResult diffResult, final List<TestBean> newBeans) {
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                //step 3 调用dispatchUpdatesTo方法
                diffResult.dispatchUpdatesTo(mAdapter);

                //step adapter设置新的数据集
                mAdapter.setNewData(newBeans);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quit();
    }
}
