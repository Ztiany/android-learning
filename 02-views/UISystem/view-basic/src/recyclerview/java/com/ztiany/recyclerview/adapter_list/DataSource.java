package com.ztiany.recyclerview.adapter_list;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 * Date : 2018-08-14 16:39
 */
public class DataSource {

    private DataSource() {
    }

    public static List<Person> crateList() {
        List<Person> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(new Person(20 + i, "name_" + i, "深圳"));
        }

        return list;
    }

}
