package com.x.vscam.global.utils;

import com.x.vscam.main.ImgFlowBean;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ProcessDataUtils {
    public static ImgFlowBean addUserInfo(ImgFlowBean data){
        int gridsCount = data.getGrids().size();
        int userCount = data.getUsers().size();
        for(int i = 0; i < gridsCount; i++){
            if(i < userCount){
                data.getGrids().get(i).setUsersBean(data.getUsers().get(i));
            }else {
                break;
            }
        }

        return data;
    }
}
