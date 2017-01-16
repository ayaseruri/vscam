package com.x.vscam.splash;

import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.StartUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_splash)
public class SplashActivity extends BaseActivity {

    private static short SPLASH_TIME = 1;

    @ViewById(R.id.logo)
    SimpleDraweeView mLogo;

    @AfterViews
    void init() {

        Observable.timer(SPLASH_TIME, TimeUnit.SECONDS)
                .compose(RxUtils.<Long>applySchedulers())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        StartUtils.startMain(SplashActivity.this);
                        finish();
                    }
                });
    }
}
