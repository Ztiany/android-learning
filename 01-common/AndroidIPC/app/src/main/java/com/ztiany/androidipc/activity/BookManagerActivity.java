package com.ztiany.androidipc.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ztiany.androidipc.IBookManager;
import com.ztiany.androidipc.IOnNewBookArrivedListener;
import com.ztiany.androidipc.R;
import com.ztiany.androidipc.model.Book;
import com.ztiany.androidipc.service.BookManagerService;

import java.util.List;

public class BookManagerActivity extends AppCompatActivity {

    private IBookManager mIBookManager;

    private EditText mBookIdEt;
    private EditText mBookNameEt;
    private InterHandler mInterHandler;
    private final String TAG = BookManagerActivity.class.getSimpleName();

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.d("BookManagerActivity", Thread.currentThread().getName());
            if (mIBookManager == null) {
                return;
            }
            mIBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIBookManager = null;
            //重连
            bindService(null);
        }
    };


    public static class InterHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("InterHandler", "new book add" + msg.obj);
        }
    }

    private IOnNewBookArrivedListener.Stub mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            Message message = Message.obtain();
            message.obj = newBook;
            mInterHandler.sendMessage(message);
        }
    };


    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mIBookManager = IBookManager.Stub.asInterface(service);
                try {
                    mIBookManager.registerListener(mIOnNewBookArrivedListener);
                    service.linkToDeath(mDeathRecipient, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "" + name.getPackageName());
            Log.d("BookManagerActivity", Thread.currentThread().getName());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        initView();
        mInterHandler = new InterHandler();
    }

    private void initView() {
        mBookIdEt = (EditText) findViewById(R.id.id_act_book_id_et);
        mBookNameEt = (EditText) findViewById(R.id.id_act_book_name_et);
    }

    public void bindService(View view) {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    public void addBook(View view) {
        String id = mBookIdEt.getText().toString();
        String name = mBookNameEt.getText().toString();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name)) {
            return;
        }
        if (mIBookManager != null) {
            try {
                mIBookManager.addBook(new Book(id, name));
                Toast.makeText(BookManagerActivity.this, "add success", Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(BookManagerActivity.this, "add fail " + e.getCause(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getBookList(View view) {
        if (mIBookManager != null) {
            try {
                List<Book> bookList = mIBookManager.getBookList();
                if (bookList != null) {
                    Log.e(TAG, bookList.toString());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(BookManagerActivity.this, "get book list fail " + e.getCause(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIBookManager == null) {
            return;
        }
        if (mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.unregisterListener(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mIBookManager = null;
        unbindService(mServiceConnection);
    }

}
