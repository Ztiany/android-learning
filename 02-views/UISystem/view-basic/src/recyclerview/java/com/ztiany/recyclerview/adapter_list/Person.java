package com.ztiany.recyclerview.adapter_list;

/**
 * @author Ztiany
 * Date : 2018-08-14 16:38
 */
public class Person {

    private int id;
    private String name;
    private String address;

    public Person() {
    }

    Person(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}