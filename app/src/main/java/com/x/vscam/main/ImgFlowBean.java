package com.x.vscam.main;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.x.vscam.global.bean.UserBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ImgFlowBean implements Serializable{

    private List<GridsBean> grids;
    private List<UserBean> users;

    public List<GridsBean> getGrids() {
        return grids;
    }

    public void setGrids(List<GridsBean> grids) {
        this.grids = grids;
    }

    public List<UserBean> getUsers() {
        return users;
    }

    public void setUsers(List<UserBean> users) {
        this.users = users;
    }

    public static class GridsBean implements Serializable, Parcelable{
        /**
         * pid : 589
         * uid : 6
         * origin : 706d8a5d1fc7d563a7ed5a0598c6f478
         * scale : 0.99929503
         * wbpid : a15b4afegw1f5jjdu3uumj20xc0xbjwe
         * preset : BL
         * unix : 1467739114
         * aperture : 2.8
         * ios : 200
         */

        private int pid;
        private int uid;
        private String origin;
        private double scale;
        private String wbpid;
        private String preset;
        private int unix;
        private String aperture;
        private String iso;
        private String userName;
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

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

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public double getScale() {
            return scale;
        }

        public void setScale(double scale) {
            this.scale = scale;
        }

        public String getWbpid() {
            return wbpid;
        }

        public void setWbpid(String wbpid) {
            this.wbpid = wbpid;
        }

        public String getPreset() {
            return preset;
        }

        public void setPreset(String preset) {
            this.preset = preset;
        }

        public int getUnix() {
            return unix;
        }

        public void setUnix(int unix) {
            this.unix = unix;
        }

        public String getAperture() {
            return aperture;
        }

        public void setAperture(String aperture) {
            this.aperture = aperture;
        }

        public String getIso() {
            return iso;
        }

        public void setIso(String ios) {
            this.iso = ios;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.pid);
            dest.writeInt(this.uid);
            dest.writeString(this.origin);
            dest.writeDouble(this.scale);
            dest.writeString(this.wbpid);
            dest.writeString(this.preset);
            dest.writeInt(this.unix);
            dest.writeString(this.aperture);
            dest.writeString(this.iso);
            dest.writeString(this.userName);
            dest.writeString(this.text);
        }

        public GridsBean() {
        }

        protected GridsBean(Parcel in) {
            this.pid = in.readInt();
            this.uid = in.readInt();
            this.origin = in.readString();
            this.scale = in.readDouble();
            this.wbpid = in.readString();
            this.preset = in.readString();
            this.unix = in.readInt();
            this.aperture = in.readString();
            this.iso = in.readString();
            this.userName = in.readString();
            this.text = in.readString();
        }

        public static final Creator<GridsBean> CREATOR = new Creator<GridsBean>() {
            @Override
            public GridsBean createFromParcel(Parcel source) {
                return new GridsBean(source);
            }

            @Override
            public GridsBean[] newArray(int size) {
                return new GridsBean[size];
            }
        };
    }
}
