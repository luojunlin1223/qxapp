package com.example.qxapp.activity.Bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

public class Post extends BmobObject {
//    上传的对应用户

    private BmobUser user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //    帖子的内容
    private String title,content,name;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
