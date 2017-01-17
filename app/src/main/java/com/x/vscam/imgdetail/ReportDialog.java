package com.x.vscam.imgdetail;

import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.main.ImgFlowBean;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import io.reactivex.functions.Consumer;
import okhttp3.ResponseBody;
import ykooze.ayaseruri.codesslib.rx.RxBus;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

/**
 * Created by wufeiyang on 2017/1/17.
 */

public class ReportDialog {
    public void init(final Context context, final ImgFlowBean.GridsBean gridsBean){

        View contentView = LayoutInflater.from(context).inflate(R.layout.view_report_dialog, null);
        final EditText reasonET = ((TextInputLayout) contentView.findViewById(R.id.reason)).getEditText();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("举报")
                .setView(contentView)
                .setMessage("请填写您的举报描述信息以方便我们的查证与处理")
                .setNegativeButton("取消", null)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String reason = reasonET.getText().toString();
                        if(TextUtils.isEmpty(reason)){
                            reasonET.setHint("不能为空");
                        }else {
                            ApiIml.getInstance(context).reportImg(gridsBean.getPid(), reason).compose(RxUtils
                                    .<ResponseBody>applySchedulers())
                                    .subscribe(new Consumer<ResponseBody>() {
                                        @Override
                                        public void accept(ResponseBody responseBody) throws Exception {

                                        }
                                    });
                            RxBus.getDefault().send(Constans.TAG_REPORT, null);
                            dialog.dismiss();
                        }
                    }
                });

        builder.show();
    }
}
