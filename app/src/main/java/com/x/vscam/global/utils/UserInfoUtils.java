package com.x.vscam.global.utils;

import android.content.Context;

import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;

import ykooze.ayaseruri.codesslib.cache.CacheUtils;

/**
 * Created by wufeiyang on 2017/1/14.
 */

public class UserInfoUtils {
    public static UserBean getUserInfo(Context context){
        return (UserBean) CacheUtils.get(context, Constans.KEY_USER_INFO, false);
    }

    public static boolean isLogin(Context context){
        return getUserInfo(context) != null;
    }
}
