package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class ThumbsupdownRecord extends BmobObject {
    private BmobUser user;
    private Recommondation recommondation;
    private int Tumbsup_count;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public Recommondation getRecommondation() {
        return recommondation;
    }

    public void setRecommondation(Recommondation recommondation) {
        this.recommondation = recommondation;
    }

    public int getTumbsup_count() {
        return Tumbsup_count;
    }

    public void setTumbsup_count(int tumbsup_count) {
        Tumbsup_count = tumbsup_count;
    }

    public int getTumbsdown_count() {
        return Tumbsdown_count;
    }

    public void setTumbsdown_count(int tumbsdown_count) {
        Tumbsdown_count = tumbsdown_count;
    }

    private int Tumbsdown_count;
}
