package com.x.vscam.register;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.bean.UserBean;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.Utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ykooze.ayaseruri.codesslib.rx.RxUtils;
import ykooze.ayaseruri.codesslib.ui.BabushkaText;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById(R.id.nick)
    TextInputLayout mNick;
    @ViewById(R.id.email)
    TextInputLayout mEmail;
    @ViewById(R.id.password)
    TextInputLayout mPassword;
    @ViewById(R.id.progress_bar)
    ContentLoadingProgressBar mProgressBar;
    @ViewById(R.id.root)
    View mRoot;
    @ViewById(R.id.user_agreement)
    BabushkaText mUserAgreement;

    @AfterViews
    void init(){
        mUserAgreement.addPiece(new BabushkaText.Piece.Builder("同意")
                .textColor(Color.parseColor("#FF878787"))
                .build());

        mUserAgreement.addPiece(new BabushkaText.Piece.Builder(" 用户协议")
                .textColor(ykooze.ayaseruri.codesslib.others.Utils.getColor(this, R.color.colorAccent))
                .build());

        mUserAgreement.display();
    }

    @AfterViews
    void init(){
        Utils.setDisplayHomeAsUp(this, mToolbar);
    }

    @Click(R.id.register_btn)
    void onRegister(){
        String nick = mNick.getEditText().getText().toString();
        if(TextUtils.isEmpty(nick)){
            Snackbar.make(mRoot, "请填写昵称", Snackbar.LENGTH_LONG).show();
            return;
        }

        String email = mEmail.getEditText().getText().toString();
        if(TextUtils.isEmpty(email)){
            Snackbar.make(mRoot, "请填写邮箱", Snackbar.LENGTH_LONG).show();
            return;
        }

        String pass = mPassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(pass)){
            Snackbar.make(mRoot, "请填写密码", Snackbar.LENGTH_LONG).show();
            return;
        }

        ApiIml.getInstance(this).register(nick, email, pass).compose(RxUtils.<UserBean>applyCache(this, Constans.KEY_USER_INFO))
                .compose(this.<UserBean>bindToLifecycle())
                .compose(RxUtils.<UserBean>applySchedulers())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(UserBean userBean) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onComplete() {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
