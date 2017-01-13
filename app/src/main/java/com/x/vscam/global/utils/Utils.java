package com.x.vscam.global.utils;

import android.content.Context;
import android.content.Intent;

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
}
