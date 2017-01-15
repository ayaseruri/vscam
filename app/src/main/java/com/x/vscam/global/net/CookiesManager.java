package com.x.vscam.global.net;

import android.content.Context;

import org.androidannotations.annotations.Click;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import ykooze.ayaseruri.codesslib.cache.CacheUtils;

/**
 * Created by wufeiyang on 2017/1/15.
 */

public class CookiesManager implements CookieJar {

    private Context mContext;

    public CookiesManager(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void saveFromResponse(HttpUrl url, final List<Cookie> cookies) {
        CacheUtils.putDisk(mContext, url.host(), new ArrayList<Cookie>(cookies.size()){
            {
                addAll(cookies);
            }
        });
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies =  (List<Cookie>) CacheUtils.get(mContext, url.host(), false);
        return null == cookies ? new ArrayList<Cookie>(0) : cookies;
    }
}
