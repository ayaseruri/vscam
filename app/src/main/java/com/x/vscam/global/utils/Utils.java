package com.x.vscam.global.utils;

import static android.R.attr.duration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by wufeiyang on 2017/1/13.
 */

public class Utils {
    public static void share(Context context, String title, String content){
        Intent share_intent = new Intent();
        share_intent.setAction(Intent.ACTION_SEND);
        share_intent.setType("text/plain");
        share_intent.putExtra(Intent.EXTRA_SUBJECT, title);
        share_intent.putExtra(Intent.EXTRA_TEXT, content);

        share_intent = Intent.createChooser(share_intent, "分享到：");
        context.startActivity(share_intent);
    }

    public static void setDisplayHomeAsUp(final AppCompatActivity activity, final Toolbar toolbar){
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    public static Snackbar getSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        return Snackbar.make(rootView, message, duration);
    }
}
