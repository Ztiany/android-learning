// IBookManager.aidl
package com.ztiany.androidipc;

// Declare any non-default types here with import statements
import java.util.List;
import com.ztiany.androidipc.model.Book;
import  com.ztiany.androidipc.IOnNewBookArrivedListener;

interface IBookManager {

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,double aDouble, String aString);
    void addBook(in Book book);
    List<Book> getBookList();
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);

}
