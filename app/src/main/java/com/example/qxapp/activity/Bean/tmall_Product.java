package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;

public class tmall_Product extends BmobObject {
    private String name;
    private String url;
    private float price;
    private int sell;

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
        this.url = url.substring(0,url.indexOf("&"));
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = Float.parseFloat(price);
    }

    public int getSell() {
        return sell;
    }

    public void setSell(String sell) {
        if(sell.isEmpty()){
            this.sell=0;
        } else if (sell.length() <=1) {
            this.sell=0;
        }else{
            sell=sell.substring(0,sell.length()-1);
            if(isNumber(sell)){
                this.sell=Integer.parseInt(sell);//9000
            }
            else{
                sell=sell.substring(0,sell.length()-1);//1.2万
                this.sell= (int) (Float.parseFloat(sell)*10000);
            }
        }
    }

    private boolean isNumber(String sell) {
        for(int i=0;i<sell.length();i++){
            if(sell.charAt(i)=='万'){
                return false;
            }
        }
        return true;
    }
}
