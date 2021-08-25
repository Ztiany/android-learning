package com.ztiany.test.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


/**
 * <pre>
 *      1.生命周期的打印
 *      2.findViewById优化
 *      3.实现OnBackPressListener，如果需要处理BackPress事件，可以重写handleBackPress方法
 * <pre>
 *
 * @author Ztiany
 *         Date : 2016-03-18 15:26
 *         Email: ztiany3@gmail.com
 */
public abstract class AbstractFragment extends Fragment {

    private String tag() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(tag(), "onAttach() called with: context = [" + context + "]");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag(), "-->onCreate  savedInstanceState   =   " + savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(tag(), "-->onCreateView container = " + container + "     savedInstanceState   =  " + savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag(), "-->onViewCreated  savedInstanceState   =   " + savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(tag(), "-->onActivityCreated savedInstanceState   =   " + savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(tag(), "-->onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(tag(), "-->onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(tag(), "-->onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(tag(), "-->onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(tag(), "-->onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(tag(), "-->onDestroy");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(tag(), "-->onDetach");
    }

    /**
     * 此方法会在与ViewPager使用时被调用
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(tag(), "-->setUserVisibleHint ==" + isVisibleToUser);
    }

    /**
     * 当hide或者show一个fragment时 方法可能会被调用
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d(tag(), "-->onHiddenChanged = " + hidden);
    }

    protected <T extends View> T findView(@IdRes int viewId) {
        if (getView() != null) {
            @SuppressWarnings("unchecked")//需要什么类型，就返回什么类型
                    T view = (T) getView().findViewById(viewId);
            return view;
        }
        return null;
    }

    /**
     * Fragment需要自己处理BackPress事件，如果不处理，就交给子Fragment处理。都不处理则由Activity处理
     *
     * @return
     */
    protected boolean handleBackPress() {
        return false;
    }

}
