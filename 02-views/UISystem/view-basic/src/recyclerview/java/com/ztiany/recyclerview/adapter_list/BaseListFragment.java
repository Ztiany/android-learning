package com.ztiany.recyclerview.adapter_list;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ztiany.view.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

/**
 * @author Ztiany
 * Date : 2018-08-14 17:11
 */
public abstract class BaseListFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.adapterBtnAdd).setOnClickListener(this::showMenu);
    }

    private void showMenu(View v) {
        String[] titles = {"add last", "add first", "add all", "remove first", "modify first"};

        PopupMenu popupMenu = new PopupMenu(requireContext(), v);
        Menu menu = popupMenu.getMenu();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            menu.add(Menu.NONE, i, i, titles[i]);
        }
        popupMenu.setGravity(Gravity.BOTTOM);
        popupMenu.setOnMenuItemClickListener(item -> {
            processItemClicked(item);
            return true;
        });
        popupMenu.show();
    }


    @SuppressWarnings("all")
    private void processItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case 0: {
                addOne();
                break;
            }
            case 1: {
                addFirst();
                break;
            }
            case 2: {
                addAll();
                break;
            }
            case 3: {
                removeFirst();
                break;
            }
            case 4: {
                modifyFirst();
                break;
            }
        }
    }

    protected abstract void modifyFirst();

    protected abstract void removeFirst();

    protected abstract void addFirst();

    protected abstract void addAll();

    protected abstract void addOne();
}
