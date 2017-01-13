package com.x.vscam.login;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.widget.ScrollView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ykooze.ayaseruri.codesslib.rx.RxActivity;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@EActivity(R.layout.activity_login)
public class LoginActivity extends RxActivity {

    @ViewById(R.id.scroll)
    ScrollView mScrollView;
    @ViewById(R.id.email)
    TextInputLayout mEmail;
    @ViewById(R.id.password)
    TextInputLayout mPassword;

    @Click(R.id.login_btn)
    void onLogin(){
        String email = mEmail.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mScrollView, "邮箱不能为空", Snackbar.LENGTH_LONG).show();
            return;
        }

        String pass = mPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(pass)){
            Snackbar.make(mScrollView, "密码不能为空", Snackbar.LENGTH_LONG).show();
        }

        ApiIml.getInstance(this).login(email.trim(), pass.trim())
                .compose(RxUtils.<UserBean>applySchedulers())
                .compose(RxUtils.<UserBean>useCache(this, Constans.KEY_USER_INFO, false))
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserBean userBean) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
