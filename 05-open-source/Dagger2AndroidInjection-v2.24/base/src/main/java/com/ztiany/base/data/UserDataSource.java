package com.ztiany.base.data;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 11:11
 */
public interface UserDataSource {

    interface Callback {

        void onLoadUser(User user);

    }

    void user(String id, Callback callback);

}
