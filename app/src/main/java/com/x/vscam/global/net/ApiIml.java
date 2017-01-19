package com.x.vscam.global.net;

import static com.x.vscam.global.Constans.BASE_URL;

import java.io.IOException;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import android.content.Context;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ykooze.ayaseruri.codesslib.net.FastJsonConverterFactory;
import ykooze.ayaseruri.codesslib.net.OkHttpBuilder;

/**
 * Created by wufeiyang on 2017/1/12.
 */

public class ApiIml {
    private static volatile ApiInterface sApiInterface;
    private static volatile OkHttpClient sOkHttpClient;

    public static ApiInterface getInstance(Context context){
        if(null == sApiInterface){
            synchronized(ApiIml.class){
                if(null == sApiInterface){
                    sApiInterface = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL)
                            .client(getOkHttpClient(context))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(FastJsonConverterFactory.create())
                            .build()
                            .create(ApiInterface.class);
                }
            }
        }
        return sApiInterface;
    }

    public static OkHttpClient getOkHttpClient(Context context){
        if(null == sOkHttpClient){
            synchronized(ApiIml.class){
                if(null == sOkHttpClient){
                    sOkHttpClient = new OkHttpBuilder()
                            .withInterceptor(new Interceptor() {
                                @Override
                                public Response intercept(Chain chain) throws IOException {
                                    Request.Builder builder = chain.request().newBuilder();
                                    builder.addHeader("referer", "https://vscam.co");
                                    return chain.proceed(builder.build());
                                }
                            })
                            .withCookie(new CookiesManager(context))
                            .withDefalutCache().getClient(context);
                }
            }
        }

        return sOkHttpClient;
    }
}
