package com.x.vscam.detail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
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
import com.x.vscam.main.ImgFlowBean;

import android.view.Window;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import ykooze.ayaseruri.codesslib.rx.RxUtils;

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

    @DimensionPixelSizeRes(R.dimen.img_detail_img_height)
    int mImgHeight;

    @AfterViews
    void init(){
        initFrescoShareElement();
        ImgFlowBean.GridsBean gridsBean = getIntent().getParcelableExtra(Constans.KEY_GRID);
        if(null != gridsBean){
            init(gridsBean);
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

    private void initFrescoShareElement(){
        Window window = getWindow();
        window.setSharedElementEnterTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
        window.setSharedElementReturnTransition(
                DraweeTransition.createTransitionSet(ScalingUtils.ScaleType.CENTER_CROP
                        , ScalingUtils.ScaleType.CENTER_CROP));
    }

    private void init(ImgFlowBean.GridsBean gridsBean){
        mImg.setImageURI(ProcessDataUtils.getImgUrlS(gridsBean));
        mDetailBoard.setUserName(gridsBean.getUserName());

        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.SIMPLIFIED_CHINESE);
        mDetailBoard.setTime(sdf.format(new Date(gridsBean.getUnix())));

        mDetailBoard.setAperture(gridsBean.getAperture());

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
}
