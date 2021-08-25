package com.ztiany.recyclerview.itemtouch;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztiany.view.utils.UnitConverter;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2017-08-30 23:51
 */
public class ItemTouchListenerFragment extends Fragment {

    private static final String TAG = ItemTouchListenerFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mRecyclerView == null ? mRecyclerView = new RecyclerView(getContext()) : mRecyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.addOnItemTouchListener(mOnItemTouchListener);
        mRecyclerView.setAdapter(new Adapter());
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new AppCompatTextView(getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(Color.BLACK);
            int padding = UnitConverter.dpToPx(10);
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextSize(24);
            textView.getPaint().setFakeBoldText(true);
            textView.setGravity(Gravity.CENTER);
            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("I am Item , position = " + position);
        }

        @Override
        public int getItemCount() {
            return 100;
        }
    }

    private RecyclerView.OnItemTouchListener mOnItemTouchListener = new RecyclerView.OnItemTouchListener() {

        /**
         * 拦截事件
         */
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d(TAG, "onInterceptTouchEvent() called with: rv = [" + rv + "], e = [" + e + "]");
            return true;
        }

        /**
         * 处理拦截的事件
         */
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d(TAG, "onTouchEvent() called with: rv = [" + rv + "], e = [" + e + "]");
        }

        /**
         * 此处是Item请求不要拦截事件
         */
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            Log.d(TAG, "onRequestDisallowInterceptTouchEvent() called with: disallowIntercept = [" + disallowIntercept + "]");
        }
    };
}
