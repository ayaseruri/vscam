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
            ArrayList<SerializableOkHttpCookies> serializabledCookies = getSerializableCookies(url);
            if(null != serializabledCookies && serializabledCookies.size() > 0){
                int serializabledCookiesSize = serializabledCookies.size();
                for (int i = 0; i< serializabledCookiesSize; i++){
                    for (Cookie cookie : cookies){
                        if(serializabledCookies.get(i).getCookies().name().equals(cookie.name())){
                            Cookie.Builder builder = new Cookie.Builder();
                            builder = builder.name(cookie.name());
                            builder = builder.value(cookie.value());
                            builder = builder.expiresAt(cookie.expiresAt());
                            builder = cookie.hostOnly() ? builder.hostOnlyDomain(cookie.domain()) : builder.domain
                                    (cookie.domain());
                            builder = builder.path(cookie.path());
                            builder = cookie.secure() ? builder.secure() : builder;
                            builder = cookie.httpOnly() ? builder.httpOnly() : builder;
                            serializabledCookies.set(i, new SerializableOkHttpCookies(builder.build()));
                            break;
                        }
                    }
                }

                for (Cookie cookie : cookies){
                    boolean alreadHave = false;
                    for (SerializableOkHttpCookies serializabledCookie : serializabledCookies){
                        if(serializabledCookie.getCookies().name().equals(cookie.name())){
                            alreadHave = true;
                            break;
                        }
                    }

                    if(!alreadHave){
                        serializabledCookies.add(new SerializableOkHttpCookies(cookie));
                    }
                }
            }else {
                serializabledCookies = new ArrayList<>(cookies.size());
                for(Cookie cookie : cookies){
                    serializabledCookies.add(new SerializableOkHttpCookies(cookie));
                }
            }

            CacheUtils.putDisk(mContext, url.host(), serializabledCookies);
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        final List<SerializableOkHttpCookies> serializableOkHttpCookies =  getSerializableCookies(url);
        return null == serializableOkHttpCookies ? Collections.<Cookie>emptyList() : new ArrayList<Cookie>
                (serializableOkHttpCookies.size()){
            {
                for (SerializableOkHttpCookies temp : serializableOkHttpCookies){
                    add(temp.getCookies());
                }
            }
        };
    }

    private ArrayList<SerializableOkHttpCookies> getSerializableCookies(HttpUrl url){
        return (ArrayList<SerializableOkHttpCookies>) CacheUtils.get
                (mContext, url.host(), false);
    }
}
