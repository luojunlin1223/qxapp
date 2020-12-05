package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class CollectRecord extends BmobObject {
    private BmobUser user;
    private Recommondation recommondation;

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
}
