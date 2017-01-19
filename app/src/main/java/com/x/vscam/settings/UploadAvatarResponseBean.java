package com.x.vscam.settings;

/**
 * Created by wufeiyang on 2017/1/15.
 */

public class UploadAvatarResponseBean {
    /**
     * uid : 111
     */

    private int uid;
    private String error;

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
}
