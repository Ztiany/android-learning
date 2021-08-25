package com.ztiany.view.custom.message_drag;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztiany.view.R;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-15 11:25
 */
public class MessageDragFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customview_message_bubble_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        MessageBubbleView.bindToView(view.findViewById(R.id.iv_red), new MessageDragListener.OnViewDragDisappearListener() {
            @Override
            public void onDisappear(View originalView) {

            }
        });


        MessageBubbleView.bindToView(view.findViewById(R.id.iv_launcher), new MessageDragListener.OnViewDragDisappearListener() {
            @Override
            public void onDisappear(View originalView) {

            }
        });


        MessageBubbleView.bindToView(view.findViewById(R.id.tv_text), new MessageDragListener.OnViewDragDisappearListener() {
            @Override
            public void onDisappear(View originalView) {

            }
        });


    }
}
