package com.ztiany.base;

import android.widget.Toast;

import com.ztiany.base.presentation.MessageUtils;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 15:04
 */
class AndroidMessageUtils implements MessageUtils {

    @Inject
    AndroidMessageUtils() {

    }

    @Override
    public void showMessage(int id) {
        Toast.makeText(AppContext.getContext(), id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(CharSequence message) {
        Toast.makeText(AppContext.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
