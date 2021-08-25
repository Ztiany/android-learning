package com.xiangxue.base.mvvm.model;

public class PagingResult {

    public PagingResult(boolean isFirstPage, boolean isEmpty, boolean hasNextPage){
        this.isFirstPage = isFirstPage;
        this.isEmpty = isEmpty;
        this.hasNextPage = hasNextPage;
    }

    public boolean isFirstPage;
    public boolean isEmpty;
    public boolean hasNextPage;
}
