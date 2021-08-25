package com.ztiany.module_b;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-09-19 15:46
 */
class Bean {

    private String mName;
    private int id;

    Bean(String name, int id) {
        mName = name;
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return id;
    }
}
