package com.ztiany.androidipc.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Book implements Parcelable{

    private String mBookId;
    private String mBookName;

    public Book() {
    }

    public Book(String bookId, String bookName) {
        mBookId = bookId;
        mBookName = bookName;
    }

    protected Book(Parcel in) {
        mBookId = in.readString();
        mBookName = in.readString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "mBookId='" + mBookId + '\'' +
                ", mBookName='" + mBookName + '\'' +
                '}';
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mBookId);
        dest.writeString(mBookName);
    }
}
