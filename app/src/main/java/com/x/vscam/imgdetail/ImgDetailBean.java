package com.x.vscam.imgdetail;

import com.x.vscam.global.bean.UserBean;

/**
 * Created by wufeiyang on 2017/1/13.
 */

public class ImgDetailBean {

    /**
     * pid : 614
     * uid : 6
     * wbpid : a15b4afegw1f70po2hjt2j20xc0xcq9v
     * scale : 1
     * origin : 1f0c75b82838d155fe17398ca910b6d0
     * text : 绫波丽
     * preset : IT
     * gps : 121.526694,31.2440805
     * look : 195
     * like : 0
     * state : 1
     * unix : 1471713060
     * user : {"uid":6,"name":"itorr","avatar":1}
     */

    private int pid;
    private int uid;
    private String wbpid;
    private int scale;
    private String origin;
    private String text;
    private String preset;
    private String gps;
    private String look;
    private String like;
    private String state;
    private int unix;
    private UserBean user;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getWbpid() {
        return wbpid;
    }

    public void setWbpid(String wbpid) {
        this.wbpid = wbpid;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getLook() {
        return look;
    }

    public void setLook(String look) {
        this.look = look;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUnix() {
        return unix;
    }

    public void setUnix(int unix) {
        this.unix = unix;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
