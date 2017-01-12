package com.x.vscam.main;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ImgFlowBean implements Serializable{

    private List<GridsBean> grids;
    private List<UsersBean> users;

    public List<GridsBean> getGrids() {
        return grids;
    }

    public void setGrids(List<GridsBean> grids) {
        this.grids = grids;
    }

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class GridsBean implements Serializable{
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
        private String ios;
        private UsersBean usersBean;

        public UsersBean getUsersBean() {
            return usersBean;
        }

        public void setUsersBean(UsersBean usersBean) {
            this.usersBean = usersBean;
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

        public String getIos() {
            return ios;
        }

        public void setIos(String ios) {
            this.ios = ios;
        }
    }

    public static class UsersBean implements Serializable{
        /**
         * uid : 6
         * name : itorr
         * avatar : 1
         */

        private int uid;
        private String name;
        private int avatar;

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
    }
}
