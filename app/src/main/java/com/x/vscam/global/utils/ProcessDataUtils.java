package com.x.vscam.global.utils;

import com.x.vscam.main.ImgFlowBean;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ProcessDataUtils {
    public static ImgFlowBean addUserInfo(ImgFlowBean data){
        for(ImgFlowBean.GridsBean gridsBean : data.getGrids()){
            for (ImgFlowBean.UsersBean usersBean : data.getUsers()){
                if(gridsBean.getUid() == usersBean.getUid()){
                    gridsBean.setUserName(usersBean.getName());
                    break;
                }
            }
        }

        return data;
    }
}
