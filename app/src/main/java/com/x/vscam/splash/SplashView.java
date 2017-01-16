package com.x.vscam.splash;

import java.util.concurrent.TimeUnit;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.view.SimpleDraweeView;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.x.vscam.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EViewGroup(R.layout.view_splash)
public class SplashView extends FrameLayout {

    @ViewById(R.id.logo)
    SimpleDraweeView mLogo;

    public SplashView(Context context) {
        super(context);
    }

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(final int time, final IOnSplashTimeEnd onSplashTimeEnd) {
        Observable.timer(time, TimeUnit.SECONDS)
                .compose(RxUtils.<Long>applySchedulers())
                .compose(RxLifecycleAndroid.<Long>bindView(SplashView.this))
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long time) throws Exception {
                        onSplashTimeEnd.onEnd();
                    }
                });
    }

    public int getLogoCenterX(){
        return (int)((mLogo.getRight() - mLogo.getLeft()) * 1.0f / 2 + mLogo.getLeft());
    }

    public int getLogoCenterY(){
        return (int)((mLogo.getBottom() - mLogo.getTop()) * 1.0f / 2 + mLogo.getTop());
    }

    public interface IOnSplashTimeEnd{
        void onEnd();
    }
}
