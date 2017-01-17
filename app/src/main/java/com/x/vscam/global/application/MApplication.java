package com.x.vscam.global.application;

import org.androidannotations.annotations.EApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.x.vscam.BuildConfig;
import com.x.vscam.R;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.net.OkHttp3ImagePipelineConfigFactory;
import com.x.vscam.main.MainActivity;

import android.app.Application;
import android.os.Environment;
import ykooze.ayaseruri.codesslib.ui.LocalDisplay;

/**
 * Created by wufeiyang on 2017/1/12.
 */
@EApplication
public class MApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        LocalDisplay.init(this);

        Beta.autoInit = true;
        Beta.autoCheckUpgrade = false;
        Beta.upgradeCheckPeriod = 60 * 1000;
        Beta.largeIconId = R.drawable.ic_launcher;
        Beta.smallIconId = R.drawable.ic_launcher;
        Beta.storageDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.showInterruptedStrategy = true;
        Beta.enableHotfix = false;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.upgradeDialogLayoutId = R.layout.view_upgrade_dialog;
        Bugly.init(getApplicationContext(), "5ecdad3de3", BuildConfig.DEBUG);

        if(!Fresco.hasBeenInitialized()){
            ImagePipelineConfig config = OkHttp3ImagePipelineConfigFactory
                    .newBuilder(this, ApiIml.getOkHttpClient(this))
                    .setDownsampleEnabled(true)
                    .build();
            Fresco.initialize(this, config);
        }
    }
}
