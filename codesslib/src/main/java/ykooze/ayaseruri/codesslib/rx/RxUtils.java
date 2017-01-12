package ykooze.ayaseruri.codesslib.rx;

import java.io.Serializable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Publisher;

import android.content.Context;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;
import ykooze.ayaseruri.codesslib.others.Utils;

/**
 * Created by wufeiyang on 16/7/7.
 */
public class RxUtils {
    private volatile static Scheduler sChedulers;

    public static <T> ObservableTransformer<T, T> applySchedulers(){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return upstream.subscribeOn(getSchedulers())
                        .unsubscribeOn(getSchedulers())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> applyFlowableSchedulers(){
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(getSchedulers())
                        .unsubscribeOn(getSchedulers())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applyCache(final Context context){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return upstream.subscribeOn(getSchedulers())
                        .unsubscribeOn(getSchedulers())
                        .map(new Function<T, T>() {
                            @Override
                            public T apply(T t) throws Exception {
                                SerializeUtils.serializationSync(context, t.getClass().getName(), t);
                                return t;
                            }
                        });
            }
        };
    }

    public static <T extends Serializable> ObservableTransformer<T, T> useCache(
            final Context context
            , final Class<? super T> t
            , final boolean delete){
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return io.reactivex.Observable.concat(io.reactivex.Observable.create(
                        new ObservableOnSubscribe<T>() {
                            @Override
                            public void subscribe(ObservableEmitter<T> e) throws Exception {
                                Object object = SerializeUtils.deserializationSync(context, t.getClass().getName(),
                                        delete);
                                e.onNext((T) object);
                                e.onComplete();
                            }
                        })
                        .subscribeOn(getSchedulers())
                        .unsubscribeOn(getSchedulers())
                        , upstream);
            }
        };
    }

    public static Scheduler getSchedulers() {
        if(null == sChedulers){
            synchronized (RxUtils.class){
                if(null == sChedulers){
                    int workNum = Math.min(Math.max(4, Utils.getNumberOfCores()) * 2 + 1, 16);
                    sChedulers = Schedulers.from(new ThreadPoolExecutor(1,
                            workNum,
                            30,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>())
                    );
                }
            }
        }

        return sChedulers;
    }
}
