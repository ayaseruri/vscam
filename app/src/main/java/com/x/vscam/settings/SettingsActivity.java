package com.x.vscam.settings;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;

import android.widget.EditText;
import android.widget.TextView;
import ykooze.ayaseruri.codesslib.rx.RxActivity;

@EActivity(R.layout.activity_settings)
public class SettingsActivity extends RxActivity {

    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @ViewById(R.id.user_name)
    TextView mUserName;
    @ViewById(R.id.introduce)
    EditText mIntroduce;
    @ViewById(R.id.my_blog)
    EditText mBlog;

    @AfterViews
    void init(){

    }
}
