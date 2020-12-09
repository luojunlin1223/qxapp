package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Proset extends BmobObject {
    private String name;
    private BmobUser user;
    private double price_low;

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

    public double getPrice_low() {
        return price_low;
    }

    public void setPrice_low(double price_low) {
        this.price_low = price_low;
    }

    public double getPrice_high() {
        return price_high;
    }

    public void setPrice_high(double price_high) {
        this.price_high = price_high;
    }

    public int getPrice_percentage() {
        return price_percentage;
    }

    public void setPrice_percentage(int price_percentage) {
        this.price_percentage = price_percentage;
    }

    private double price_high;
    private int price_percentage;
}
