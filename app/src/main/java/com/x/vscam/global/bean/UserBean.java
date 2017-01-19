package com.x.vscam.global.bean;

import java.io.Serializable;

/**
 * Created by wufeiyang on 2017/1/13.
 */

public class UserBean implements Serializable{

    /**
     * uid : 4
     * name : wsph123
     * avatar : 4
     * des : 我的个人简介
     * url : http://mouto.org
     */

    private int uid;
    private String name;
    private int avatar;
    private String error;
    private String des;
    private String url;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
