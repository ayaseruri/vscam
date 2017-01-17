package com.x.vscam.imgdetail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;
import com.x.vscam.R;
import com.x.vscam.global.Constans;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.net.ApiInterface;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.global.utils.Utils;
import com.x.vscam.main.ImgFlowBean;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ykooze.ayaseruri.codesslib.rx.RxBus;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

@OptionsMenu(R.menu.men_img_detail)
@EActivity(R.layout.activity_img_detail)
public class ImgDetailActivity extends BaseActivity {

    @ViewById(R.id.scroll)
    ReboundScrollView mScroll;
    @ViewById(R.id.img)
    SimpleDraweeView mImg;
    @ViewById(R.id.map)
    SimpleDraweeView mMap;
    @ViewById(R.id.img_detail_board)
    ImgDetailBoard mDetailBoard;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @DimensionPixelSizeRes(R.dimen.img_detail_img_height)
    int mImgHeight;

    private ImgFlowBean.GridsBean mGridsBean;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisposable = RxBus.getDefault().registerOnUiThread(new RxBus.ReceiveOnUiThread() {
            @Override
            public void OnReceive(String tag, Object object) {
                if(tag.equals(Constans.TAG_REPORT)){
                    Snackbar.make(mScroll, "已经发送举报", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @AfterViews
    void init(){
        initFrescoShareElement();
        Utils.setDisplayHomeAsUp(this, mToolbar);

        mGridsBean = getIntent().getParcelableExtra(Constans.KEY_GRID);
        if(null != mGridsBean){
            init(mGridsBean);
        }

        mScroll.setIOnReBound(new ReboundScrollView.IOnReBound() {
            @Override
            public void onOverScrollTop(int pix) {

            }

            @Override
            public void onOverScrollBottom(int pix) {
                Logger.d("pix:    " + pix);
            }
        });
    }

    private void init(final ImgFlowBean.GridsBean gridsBean){
        mImg.setImageURI(ProcessDataUtils.getImgUrlS(gridsBean));
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartUtils.startImgViewer(ImgDetailActivity.this, ProcessDataUtils.getImgUrlS(gridsBean),
                        ActivityOptionsCompat.makeSceneTransitionAnimation(ImgDetailActivity.this, mImg,
                                getString(R.string.img_transition_name)));
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.SIMPLIFIED_CHINESE);

        mDetailBoard.setUserName(gridsBean.getUserName());
        mDetailBoard.setTime(sdf.format(new Date(gridsBean.getUnix())));
        mDetailBoard.setFliterName(gridsBean.getPreset());
        mDetailBoard.setDescription(gridsBean.getText());
        mDetailBoard.setios(gridsBean.getIso());
        if(!TextUtils.isEmpty(gridsBean.getAperture())){
            mDetailBoard.setAperture(String.format("%.1f", Float.valueOf(gridsBean.getAperture())));
        }

        final ApiInterface api = ApiIml.getInstance(this);
        api.getImgDetail(gridsBean.getPid())
                .flatMap(new Function<ImgDetailBean, ObservableSource<MapBean>>() {
                    @Override
                    public ObservableSource<MapBean> apply(ImgDetailBean imgDetailBean) throws Exception {
                        return api.getMap(imgDetailBean.getGps()).compose(RxUtils.<MapBean>applySchedulers());
                    }
                }).subscribeOn(RxUtils.getSchedulers())
                .subscribe(new Consumer<MapBean>() {
                    @Override
                    public void accept(MapBean mapBean) throws Exception {
                        mMap.setImageURI(Constans.BASE_URL + "/x/" + mapBean.getUrl());
                    }
                });
    }

    @OptionsItem(R.id.share)
    void onShare(){
        if(null != mGridsBean){
            Utils.share(ImgDetailActivity.this, "[VSCAM]" + mGridsBean.getText(), "https://vscam.co/#!g/"
                    + mGridsBean.getPid());
        }
    }

    @OptionsItem(R.id.report)
    void onReport(){
        if(null != mGridsBean){
            ReportDialog reportDialog = new ReportDialog();
            reportDialog.init(this, mGridsBean);
        }
    }

    @Override
    protected void onDestroy() {
        if(null != mDisposable && !mDisposable.isDisposed()){
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    private void initFrescoShareElement(){
        Window window = getWindow();
        window.setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
        window.setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
    }
}
