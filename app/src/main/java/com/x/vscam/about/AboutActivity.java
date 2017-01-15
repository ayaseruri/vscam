package com.x.vscam.about;

import org.androidannotations.annotations.EActivity;

import com.x.vscam.R;
import com.x.vscam.global.ui.BaseActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import ykooze.ayaseruri.codesslib.others.Utils;

@EActivity
public class AboutActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle(Utils.getAppName(this));

        setContentView(new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ic_launcher)
                .addItem(versionElement)
                .addEmail("ayaseruri@foxmail.com")
                .addWebsite("http://vscam.co/")
                .addPlayStore("com.x.vscam")
                .addGitHub("vscam")
                .create());
    }
}
