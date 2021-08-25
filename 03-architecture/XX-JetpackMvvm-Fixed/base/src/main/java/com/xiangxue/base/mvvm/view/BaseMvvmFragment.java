package com.xiangxue.base.mvvm.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.xiangxue.base.R;
import com.xiangxue.base.loadsir.CustomCallback;
import com.xiangxue.base.loadsir.EmptyCallback;
import com.xiangxue.base.loadsir.ErrorCallback;
import com.xiangxue.base.loadsir.LoadingCallback;
import com.xiangxue.base.loadsir.TimeoutCallback;
import com.xiangxue.base.mvvm.viewmodel.BaseMvvmViewModel;
import com.xiangxue.base.mvvm.viewmodel.ViewStatus;
import com.xiangxue.base.utils.ToastUtil;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

public abstract class BaseMvvmFragment<VIEW extends ViewDataBinding, VIEWMODEL extends BaseMvvmViewModel, DATA> extends Fragment implements Observer {
    protected VIEWMODEL viewModel;
    protected VIEW viewDataBinding;

    protected abstract String getFragmentTag();

    public abstract @LayoutRes
    int getLayoutId();

    public abstract VIEWMODEL getViewModel();

    private LoadService mLoadService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getFragmentTag(), "onCreate.");
    }

    protected abstract View getLoadSirView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d(getFragmentTag(), "onCreateView.");
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        LoadSir loadSir = new LoadSir.Builder()
                .addCallback(new ErrorCallback())//添加各种状态页
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)//设置默认状态页
                .build();
        mLoadService = loadSir.register(getLoadSirView(), new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                viewModel.refresh();
            }
        });
        return mLoadService.getLoadLayout();
    }

    public abstract void onNetworkResponded(List<DATA> dataList, boolean isDataUpdated);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(getFragmentTag(), "onViewCreated.");
        viewModel = getViewModel();
        getLifecycle().addObserver(viewModel);
        viewModel.dataList.observe(this, this);
        viewModel.viewStatusLiveData.observe(this, this);
        onViewCreated();
    }

    protected abstract void onViewCreated();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(getContext());
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDetach");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onResume");
    }

    @Override
    public void onDestroy() {
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(getFragmentTag(), "Activity:" + getActivity() + " Fragment:" + this + ": " + "onDestroyView");
        super.onDestroyView();
    }

    public void setLoadSir(View view) {

    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus && mLoadService != null) {
            switch ((ViewStatus) o) {
                case LOADING:
                    mLoadService.showCallback(LoadingCallback.class);
                    break;
                case EMPTY:
                    mLoadService.showCallback(EmptyCallback.class);
                    break;
                case SHOW_CONTENT:
                    mLoadService.showSuccess();
                    break;
                case NO_MORE_DATA:
                    ToastUtil.show(getString(R.string.no_more_data));
                    break;
                case REFRESH_ERROR: {
                    ObservableArrayList value = (ObservableArrayList) viewModel.dataList.getValue();
                    if (value != null && value.size() == 0) {
                        mLoadService.showCallback(ErrorCallback.class);
                    } else {
                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    }
                    break;
                }
                case LOAD_MORE_FAILED:
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
            }
            onNetworkResponded(null, false);
        } else if (o instanceof List) {
            onNetworkResponded((List<DATA>) o, true);
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }
}
