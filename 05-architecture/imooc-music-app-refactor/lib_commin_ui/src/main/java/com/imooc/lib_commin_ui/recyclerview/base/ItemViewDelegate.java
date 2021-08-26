package com.imooc.lib_commin_ui.recyclerview.base;

/**
 *
 */
public interface ItemViewDelegate<T> {

  int getItemViewLayoutId();

  boolean isForViewType(T item, int position);

  void convert(ViewHolder holder, T t, int position);
}
