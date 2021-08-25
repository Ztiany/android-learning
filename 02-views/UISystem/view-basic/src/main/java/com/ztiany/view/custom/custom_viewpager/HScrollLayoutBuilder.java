package com.ztiany.view.custom.custom_viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;


public class HScrollLayoutBuilder {

    public static HScrollLayout buildHScrollLayout(Context context) {
        HScrollLayout scrollLayout = new HScrollLayout(context);
        LinearLayout.LayoutParams lp;
        ListView listView;
        for (int i = 0; i < 3; i++) {
            listView = new ListView(context);
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            listView.setLayoutParams(lp);
            listView.setAdapter(new Adapter(context, i));
            scrollLayout.addView(listView);
        }
        return scrollLayout;
    }

    private static class Adapter extends BaseAdapter {

        private final int mType;
        private final Context mContext;

        Adapter(Context context, int type) {
            mContext = context;
            mType = type;
        }

        @Override
        public int getCount() {
            return 100;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView textView = new AppCompatTextView(mContext);
                textView.setPadding(40, 40, 40, 40);
                textView.setGravity(Gravity.CENTER);
                convertView = textView;
                if (mType == 0) {
                    textView.setTextColor(Color.BLUE);
                } else if (mType == 1) {
                    textView.setTextColor(Color.RED);

                } else {
                    textView.setTextColor(Color.GREEN);
                }
            }
            TextView textView = (TextView) convertView;
            textView.setText("position = " + position);
            return convertView;
        }
    }

}
    

