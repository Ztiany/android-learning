package com.ztiany.recyclerview.diffutil;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-25 16:29
 */
class TestBean {

    private int id;
    private int drawableId;
    private String des;

    TestBean() {
    }

    public TestBean(int id, int drawableId, String des) {
        this.id = id;
        this.drawableId = drawableId;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
