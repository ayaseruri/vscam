package com.x.vscam.global.utils;

import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.main.ImgFlowBean;

import android.text.TextUtils;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ProcessDataUtils {
    public static ImgFlowBean addUserInfo(ImgFlowBean data){
        for(ImgFlowBean.GridsBean gridsBean : data.getGrids()){
            for (UserBean usersBean : data.getUsers()){
                if(gridsBean.getUid() == usersBean.getUid()){
                    gridsBean.setUserName(usersBean.getName());
                    break;
                }
            }
        }

        return data;
    }

    public static String getImgUrlS(ImgFlowBean.GridsBean gridsBean){
        String imgUrl;
        if(TextUtils.isEmpty(gridsBean.getWbpid())){
            imgUrl = String.format(Constans.OG_IMG_URL_PRE, gridsBean.getOrigin());
        }else {
            imgUrl = String.format(Constans.WB_IMG_URL_PRE, gridsBean.getWbpid());
        }
        return imgUrl;
    }

    public static String getAvatar(UserBean userBean){
        return 0 == userBean.getAvatar() ? "" : "http://vscam.co/avatar/b/" + userBean.getUid() + ".jpg";
    }
}
