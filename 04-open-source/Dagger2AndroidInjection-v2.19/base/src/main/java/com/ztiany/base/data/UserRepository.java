package com.ztiany.base.data;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 11:16
 */
public class UserRepository implements UserDataSource {

    @Inject
    UserRepository() {

    }

    @Override
    public void user(String id, final Callback callback) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    User user = new User();
                    user.setAvatar("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=697130940,3433821967&fm=27&gp=0.jpg");
                    user.setId("23");
                    user.setName("Ztiany");
                    callback.onLoadUser(user);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
