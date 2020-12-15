package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Proset extends BmobObject {
    private String name;
    private BmobUser user;
    private int price_low;
    private int price_high;
    private String brand;
    private String where;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }


    public int getPrice_low() {
        return price_low;
    }

    public void setPrice_low(String price_low) {
        this.price_low = Integer.parseInt(price_low);
    }

    public int getPrice_high() {
        return price_high;
    }

    public void setPrice_high(String price_high) {
        this.price_high =Integer.parseInt(price_high);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }




}
