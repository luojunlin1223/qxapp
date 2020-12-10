package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Proset extends BmobObject {
    private String name;
    private BmobUser user;
    private int price_low;

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


    public int getPrice_percentage() {
        return price_percentage;
    }

    public void setPrice_percentage(int price_percentage) {
        this.price_percentage = price_percentage;
    }

    private int price_high;
    private int price_percentage;
}
