package com.x.vscam.global.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
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
        if(null != cookies && cookies.size() > 0){
            CacheUtils.putDisk(mContext, url.host(), new ArrayList<SerializableOkHttpCookies>(cookies.size()){
                {
                    for(Cookie cookie : cookies){
                        add(new SerializableOkHttpCookies(cookie));
                    }
                }
            });
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        final List<SerializableOkHttpCookies> serializableOkHttpCookies =  (List<SerializableOkHttpCookies>) CacheUtils.get
                (mContext, url.host(), false);
        return null == serializableOkHttpCookies ? Collections.<Cookie>emptyList() : new ArrayList<Cookie>
                (serializableOkHttpCookies.size()){
            {
                for (SerializableOkHttpCookies temp : serializableOkHttpCookies){
                    add(temp.getCookies());
                }
            }
        };
    }
}
