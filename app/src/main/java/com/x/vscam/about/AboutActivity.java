package com.x.vscam.about;

import org.androidannotations.annotations.EActivity;

import com.x.vscam.R;
import com.x.vscam.global.ui.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import ykooze.ayaseruri.codesslib.others.Utils;

@EActivity
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle("Version " + Utils.getVerName(this));

        View aboutView = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_logo)
                .addItem(versionElement)
                .addEmail("ayaseruri@foxmail.com")
                .setDescription("手机拍摄、胶片味、意识流")
                .addWebsite("http://vscam.co/")
                .addPlayStore("com.x.vscam")
                .addGitHub("ayaseruri/vscam")
                .create();
        aboutView.setBackgroundResource(R.color.colorPrimary);
        setContentView(aboutView);
    }
}
