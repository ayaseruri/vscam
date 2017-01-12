package com.x.vscam.splash;

import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.EActivity;

import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.StartUtils;

import android.os.Bundle;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import ykooze.ayaseruri.codesslib.rx.RxUtils;
@EActivity
public class SplashActivity extends BaseActivity {

    private static short SPLASH_TIME = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Observable.timer(SPLASH_TIME, TimeUnit.SECONDS)
                .compose(this.<Long>bindToLifecycle())
                .subscribeOn(RxUtils.getSchedulers())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        StartUtils.startMain(SplashActivity.this);
                        finish();
                    }
                });
    }
}
