package com.ztiany.androidipc.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.ztiany.androidipc.IBookManager;
import com.ztiany.androidipc.IOnNewBookArrivedListener;
import com.ztiany.androidipc.model.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBooks;
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners;
    private AtomicBoolean mSelfIsRunning = new AtomicBoolean(true);

    public BookManagerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSelfIsRunning.set(false);
        mListeners = new RemoteCallbackList<>();
        mBooks = new CopyOnWriteArrayList<>();
        mBooks.add(new Book("001", "Android 群英传"));
        mBooks.add(new Book("002", "Android 开发艺术探索"));
        new Thread(new AddBookRunnable()).start();
    }

    @Override
    public void onDestroy() {
        mSelfIsRunning.set(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.ztiany.androidipc.IBookManager");
        if (check == PackageManager.PERMISSION_DENIED) {
            Log.d("BookManagerService", "PERMISSION_DENIED");
            return null;
        }
        Log.d("BookManagerService", "PERMISSION_OK");
        return new BookManager();
    }


    private class BookManager extends IBookManager.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int check = checkCallingOrSelfPermission("com.ztiany.androidipc.IBookManager");
            if (check == PackageManager.PERMISSION_DENIED) {
                Log.d("BookManagerService", "PERMISSION_DENIED");
                return false;
            }
            Log.d("BookManagerService", "PERMISSION_OK");
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                if(!packages[0].startsWith("com.ztiany")){
                    Log.d("BookManagerService", "package name error");
                    return false;
                }
            }
            Log.d("BookManagerService", "package name right");
            return super.onTransact(code, data, reply, flags);
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBooks.add(book);
            onNewBookAdd(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (listener != null) {
                mListeners.register(listener);
                Log.d("BookManager", "register success");
            }
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if (listener != null) {
                mListeners.unregister(listener);
                Log.d("BookManager", "unregister success");
            } else {
                Log.d("BookManager", "unregister fail");
            }
        }
    }

    /**
     * add
     */
    private void onNewBookAdd(Book book) throws RemoteException {
        final int N = mListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener listener = mListeners.getBroadcastItem(i);
            if (listener != null) {
                listener.onNewBookArrived(book);
            }
        }
        mListeners.finishBroadcast();
    }

    /**
     * 不断添加书的任务
     */
    private class AddBookRunnable implements Runnable {

        int i = 4;

        @Override
        public void run() {
            while (!mSelfIsRunning.get()) {
                i++;
                Book book = null;
                try {
                    if (mBooks != null) {
                        book = new Book(String.valueOf(i), "book haha" + i);
                        mBooks.add(book);
                    }
                    Thread.sleep(3000);
                    try {
                        if (book != null)
                            onNewBookAdd(book);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
