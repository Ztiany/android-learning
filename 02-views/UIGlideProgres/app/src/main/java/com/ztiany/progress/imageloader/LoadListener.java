package com.ztiany.progress.imageloader;

public interface LoadListener<T> {

    default void onLoadStart() {
    }

    default void onLoadSuccess(T resource) {
    }

    default void onLoadFail() {
    }

}
