package com.x.vscam.global.utils;

import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;

import android.content.Context;
import ykooze.ayaseruri.codesslib.cache.CacheUtils;

/**
 * Created by wufeiyang on 2017/1/14.
 */

public class UserInfoUtils {

    public static UserBean getUserInfo(Context context){
        return (UserBean) CacheUtils.get(context, Constans.KEY_USER_INFO, false);
    }

    public static void saveUserInfo(Context context, UserBean userBean){
        CacheUtils.putDisk(context, Constans.KEY_USER_INFO, userBean);
    }

    public static boolean isLogin(Context context){
        return getUserInfo(context) != null;
    }
}
