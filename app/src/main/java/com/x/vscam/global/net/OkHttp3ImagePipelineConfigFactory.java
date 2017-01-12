package com.x.vscam.global.net;

import static io.reactivex.plugins.RxJavaPlugins.onError;

import java.util.HashMap;
import java.util.Map;

import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.BaseProducerContextCallbacks;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;

import android.content.Context;
import android.net.Uri;
import android.os.SystemClock;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

public class OkHttp3ImagePipelineConfigFactory {

    public static ImagePipelineConfig.Builder newBuilder(Context context, OkHttpClient okHttpClient) {
        return ImagePipelineConfig.newBuilder(context).setNetworkFetcher(new OkHttp3NetworkFetcher(okHttpClient));
    }

    private static class OkHttp3NetworkFetcher extends BaseNetworkFetcher<OkHttp3NetworkFetchState> {
        private static final String QUEUE_TIME = "queue_time";
        private static final String FETCH_TIME = "fetch_time";
        private static final String TOTAL_TIME = "total_time";
        private static final String IMAGE_SIZE = "image_size";
        private final OkHttpClient mOkHttpClient;

        public OkHttp3NetworkFetcher(OkHttpClient okHttpClient) {
            mOkHttpClient = okHttpClient;
        }

        @Override
        public OkHttp3NetworkFetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
            return new OkHttp3NetworkFetchState(consumer, producerContext);
        }

        @Override
        public void fetch(final OkHttp3NetworkFetchState fetchState, final Callback callback) {
            fetchState.submitTime = SystemClock.elapsedRealtime();

            final Uri uri = fetchState.getUri();

            final Request request = new Request.Builder()
                    .cacheControl(new CacheControl.Builder().noStore().build())
                    .url(uri.toString())
                    .get()
                    .build();

            final Call call = mOkHttpClient.newCall(request);

            fetchState.getContext().addCallbacks(new BaseProducerContextCallbacks() {
                @Override
                public void onCancellationRequested() {
                    RxUtils.getSchedulers().scheduleDirect(new Runnable() {
                        @Override
                        public void run() {
                            call.cancel();
                        }
                    });
                }
            });

            Observable.create(new ObservableOnSubscribe<Response>() {
                @Override
                public void subscribe(ObservableEmitter<Response> e) throws Exception {
                    e.onNext(call.execute());
                    e.onComplete();
                }
            }).subscribeOn(RxUtils.getSchedulers())
                    .subscribe(new io.reactivex.functions.Consumer<Response>() {
                        @Override
                        public void accept(Response response) throws Exception {
                            fetchState.responseTime = SystemClock.elapsedRealtime();
                            final ResponseBody body = response.body();
                            try {
                                long contentLength = body.contentLength();
                                if (contentLength < 0) {
                                    contentLength = 0;
                                }
                                callback.onResponse(body.byteStream(), (int) contentLength);
                            } catch (Exception e) {
                                onError(e);
                            } finally {
                                try {
                                    body.close();
                                } catch (Exception e) {
                                    onError(e);
                                }
                            }
                        }
                    }, new io.reactivex.functions.Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            handleException(call, throwable, callback);
                        }
                    });
        }

        @Override
        public void onFetchCompletion(OkHttp3NetworkFetchState fetchState, int byteSize) {
            fetchState.fetchCompleteTime = SystemClock.elapsedRealtime();
        }

        @Override
        public Map<String, String> getExtraMap(OkHttp3NetworkFetchState fetchState, int byteSize) {
            Map<String, String> extraMap = new HashMap<>(4);
            extraMap.put(QUEUE_TIME, Long.toString(fetchState.responseTime - fetchState.submitTime));
            extraMap.put(FETCH_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.responseTime));
            extraMap.put(TOTAL_TIME, Long.toString(fetchState.fetchCompleteTime - fetchState.submitTime));
            extraMap.put(IMAGE_SIZE, Integer.toString(byteSize));
            return extraMap;
        }

        /**
         * Handles exceptions.
         * <p/>
         * <p> OkHttp notifies callers of cancellations via an IOException. If IOException is caught
         * after request cancellation, then the exception is interpreted as successful cancellation
         * and onCancellation is called. Otherwise onFailure is called.
         */
        private void handleException(final Call call, final Throwable e, final Callback callback) {
            if (call.isCanceled()) {
                callback.onCancellation();
            } else {
                callback.onFailure(e);
            }
        }
    }

    private static class OkHttp3NetworkFetchState extends FetchState {
        public long submitTime;
        public long responseTime;
        public long fetchCompleteTime;

        public OkHttp3NetworkFetchState(Consumer<EncodedImage> consumer, ProducerContext context) {
            super(consumer, context);
        }

    }
}
