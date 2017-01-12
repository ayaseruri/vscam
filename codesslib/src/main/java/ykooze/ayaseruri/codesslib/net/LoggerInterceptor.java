package ykooze.ayaseruri.codesslib.net;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Created by wufeiyang on 16/7/22.
 */
public class LoggerInterceptor implements Interceptor {

    private static final String SENDING_REQUSET = "发送请求: %s$1";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Logger.d(String.format(SENDING_REQUSET, request.url()));

        return chain.proceed(request);
    }
}
