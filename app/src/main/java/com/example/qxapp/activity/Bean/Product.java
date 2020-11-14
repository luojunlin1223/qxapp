package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;

public class Product extends BmobObject {
    private String name;
    private String url;
    private float price;
    private String where;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(String price) {
        if(price.isEmpty()){
            this.price=0;
        }else {
            this.price=Float.parseFloat(price);
        }
    }

}

