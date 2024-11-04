package com.ztiany.androidipc;

import java.util.List;
import com.ztiany.androidipc.model.Book;
import  com.ztiany.androidipc.IOnNewBookArrivedListener;

interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

    void registerListener(IOnNewBookArrivedListener listener);

    void unregisterListener(IOnNewBookArrivedListener listener);

}