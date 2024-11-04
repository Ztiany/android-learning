package com.ztiany.test;

import android.os.Parcel;
import android.os.Parcelable;


public class BeanA implements Parcelable {

    private String mName;
    private String mPhone;

    public static Class sClass = R.class;

    public BeanA() {
    }

    @SuppressWarnings("all")
    protected BeanA(Parcel in) {
        mName = in.readString();
        mPhone = in.readString();
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public static final Creator<BeanA> CREATOR = new Creator<BeanA>() {
        @Override
        public BeanA createFromParcel(Parcel in) {
            return new BeanA(in);
        }

        @Override
        public BeanA[] newArray(int size) {
            return new BeanA[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mPhone);
    }

    public String getName() {
        return mName;
    }

    public String getPhone() {
        return mPhone;
    }

}
