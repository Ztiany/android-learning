package com.dongnao.dnrouter.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Lance
 * @date 2018/2/26
 */

public class TestParcelable implements Parcelable {
    private int id;
    private String text;

    public TestParcelable(int id, String text) {
        this.id = id;
        this.text = text;
    }

    protected TestParcelable(Parcel in) {
        id = in.readInt();
        text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestParcelable> CREATOR = new Creator<TestParcelable>() {
        @Override
        public TestParcelable createFromParcel(Parcel in) {
            return new TestParcelable(in);
        }

        @Override
        public TestParcelable[] newArray(int size) {
            return new TestParcelable[size];
        }
    };

    @Override
    public String toString() {
        return "TestParcelable{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

}
