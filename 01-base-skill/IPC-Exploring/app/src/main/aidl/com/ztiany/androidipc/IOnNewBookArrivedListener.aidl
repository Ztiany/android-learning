package com.ztiany.androidipc;

import java.util.List;
import com.ztiany.androidipc.model.Book;

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}