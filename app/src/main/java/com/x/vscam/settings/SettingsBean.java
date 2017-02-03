package com.x.vscam.settings;

/**
 * Created by wufeiyang on 2017/2/3.
 */

public class SettingsBean {
    /**
     * des : 一个少年
     * url : http://ayaseruri.net
     */

    private String des;
    private String url;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
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
