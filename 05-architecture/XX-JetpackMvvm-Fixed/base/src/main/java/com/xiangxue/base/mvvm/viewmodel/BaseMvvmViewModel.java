package com.xiangxue.base.mvvm.viewmodel;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.xiangxue.base.mvvm.model.BaseMvvmModel;
import com.xiangxue.base.mvvm.model.IBaseModelListener;
import com.xiangxue.base.mvvm.model.PagingResult;

import java.util.List;

public abstract class BaseMvvmViewModel<MODEL extends BaseMvvmModel, DATA> extends ViewModel implements LifecycleObserver, IBaseModelListener<List<DATA>> {
    public MutableLiveData<List<DATA>> dataList = new MutableLiveData<>();
    protected MODEL model;
    public MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public BaseMvvmViewModel() {

    }

    public abstract MODEL createModel();

    public void refresh() {
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        createAndRegisterModel();
        if (model != null) {
            model.refresh();
        }
    }

    private void createAndRegisterModel() {
        if (model == null) {
            model = createModel();
            if (model != null) {
                model.register(this);
            } else {
                // Throw exception.
            }
        }
    }

    public void getCachedDataAndLoad() {
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        createAndRegisterModel();
        if (model != null) {
            model.getCachedDataAndLoad();
        }
    }

    public void loadNextPage() {
        createAndRegisterModel();
        if (model != null) {
            model.loadNextPage();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (model != null) {
            model.cancel();
        }
    }

    @Override
    public void onLoadSuccess(BaseMvvmModel model, List<DATA> data, PagingResult... pagingResult) {
        if (model.isPaging()) {
            if (pagingResult[0].isEmpty) {
                if (pagingResult[0].isFirstPage) {
                    viewStatusLiveData.postValue(ViewStatus.EMPTY);
                } else {
                    viewStatusLiveData.postValue(ViewStatus.NO_MORE_DATA);
                }
            } else {
                if (pagingResult[0].isFirstPage) {
                    dataList.postValue(data);
                } else {
                    dataList.getValue().addAll(data);
                    dataList.postValue(dataList.getValue());
                }
                viewStatusLiveData.postValue(ViewStatus.SHOW_CONTENT);
            }
        } else {
            dataList.postValue(data);
            viewStatusLiveData.postValue(ViewStatus.SHOW_CONTENT);
        }
    }

    @Override
    public void onLoadFail(BaseMvvmModel model, String message, PagingResult... result) {
        errorMessage.postValue(message);
        if (result != null && result.length > 0 && result[0].isFirstPage) {
            viewStatusLiveData.postValue(ViewStatus.REFRESH_ERROR);
        } else {
            viewStatusLiveData.postValue(ViewStatus.LOAD_MORE_FAILED);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        if(dataList == null || dataList.getValue() == null || dataList.getValue().size() == 0) {
            createAndRegisterModel();
            model.getCachedDataAndLoad();
        } else {
            dataList.postValue(dataList.getValue());
            viewStatusLiveData.postValue(viewStatusLiveData.getValue());
        }
    }
}
