package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;

public class Product extends BmobObject {
    private String name;
    private String url;
    private String imageurl;

    public String getInfro() {
        return infro;
    }

    public void setInfro(String infro) {
        this.infro = infro;
    }

    private double price;
    private String where;
    private String key;
    private String infro;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(String price) {
        if(price.isEmpty()){
            this.price=0.0;
        }else if(price.contains("-")){
            this.price=Double.parseDouble(price.substring(0,5));
        }else{
            this.price=Double.parseDouble(price);
        }

    }
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}

