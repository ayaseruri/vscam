package com.x.vscam.main;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.DraweeTransition;
import com.facebook.drawee.view.SimpleDraweeView;
import com.x.vscam.R;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.net.ApiInterface;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;
import com.x.vscam.global.utils.UserInfoUtils;

import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import ykooze.ayaseruri.codesslib.adapter.RecyclerAdapter;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;
import ykooze.ayaseruri.codesslib.others.ServerException;
import ykooze.ayaseruri.codesslib.ui.CommonRecyclerView;
import ykooze.ayaseruri.codesslib.ui.GridSpacingItemDecoration;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final String TAG = "main";
    private static final short SPAN_COUNT = 2;
    private static final short PAGE_SIZE = 30;
    private static final short PRE_LOADING_PADDING = 20;//小于PAGE_SIZE的任意一个值

    @ViewById(R.id.activity_main)
    View mRoot;
    @ViewById(R.id.recycler)
    CommonRecyclerView mRecyclerView;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @ViewById(R.id.avatar)
    SimpleDraweeView mAvatar;
    @DimensionPixelSizeRes(R.dimen.img_flow_spacing)
    int mImgFlowSpacing;

    private ApiInterface mApiInterface;
    private int mLastUnix;

    @Override
    protected void onResume() {
        super.onResume();
        GenericDraweeHierarchy hierarchy = mAvatar.getHierarchy();
        if(UserInfoUtils.isLogin(this)){
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            hierarchy.setRoundingParams(RoundingParams.asCircle().setRoundAsCircle(true));
            mAvatar.setImageURI(ProcessDataUtils.getAvatar(UserInfoUtils.getUserInfo(this).getUid()));
        }else {
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            hierarchy.setRoundingParams(RoundingParams.asCircle().setRoundAsCircle(false));
            mAvatar.setImageURI(Uri.parse("res:///" + R.mipmap.ic_login));
        }
    }

    @AfterViews
    void init(){
        initFrescoShareElement();

        mApiInterface = ApiIml.getInstance(MainActivity.this);

        mRecyclerView.setPreLoadPadding(PRE_LOADING_PADDING);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(SPAN_COUNT
                , mImgFlowSpacing
                , true
                , false
                , false));
        mRecyclerView.init(new CommonRecyclerView.ICommonRecyclerView<ImgFlowBean.GridsBean>() {
            @Override
            public List<ImgFlowBean.GridsBean> getFirstInData() throws Exception {
                return  (List<ImgFlowBean.GridsBean>) SerializeUtils.deserializationSync(MainActivity.this, TAG, true);
            }

            @Override
            public List<ImgFlowBean.GridsBean> getRefreshData() throws Exception {
                ImgFlowBean data = mApiInterface.getImgFlow(0).execute().body();
                data = ProcessDataUtils.addUserInfo(data);
                SerializeUtils.serializationSync(MainActivity.this, TAG, data.getGrids());
                return data.getGrids();
            }

            @Override
            public List<ImgFlowBean.GridsBean> getLoadMoreData() throws Exception {
                ImgFlowBean data = mApiInterface.getImgFlow(mLastUnix).execute().body();
                data = ProcessDataUtils.addUserInfo(data);
                return data.getGrids();
            }

            @Override
            public boolean isEnd(List<ImgFlowBean.GridsBean> ls) {
                return ls.size() != PAGE_SIZE;
            }

            @Override
            public void onHandleData(List<ImgFlowBean.GridsBean> gridsBeen) {
                if(0 != gridsBeen.size()){
                    mLastUnix = gridsBeen.get(gridsBeen.size() - 1).getUnix();
                }
            }

            @Override
            public void uiLoadingStart(@CommonRecyclerView.LoadingType int loadingType) {
                mRefreshLayout.setRefreshing(true);
            }

            @Override
            public void uiLoadingError(@CommonRecyclerView.LoadingType int loadingType, Throwable e) {
                if(e instanceof ServerException){
                    Snackbar.make(mRoot, R.string.data_error, Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(mRoot, R.string.net_error, Snackbar.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }

            @Override
            public void uiLoadingComplete(@CommonRecyclerView.LoadingType int loadingType) {
                mRefreshLayout.setRefreshing(false);
                if(CommonRecyclerView.TYPE_END == loadingType){
                    Snackbar.make(mRoot, R.string.already_end, Snackbar.LENGTH_LONG).show();
                }
            }
        }, new RecyclerAdapter<ImgFlowBean.GridsBean>() {
            @Override
            protected Item onCreateItemView(ViewGroup parent, int viewType) {
                return ImgFlowItem_.build(parent.getContext());
            }
        });

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshData();
            }
        });

        mRecyclerView.refreshData();
    }

    @Click(R.id.avatar)
    void onAvatar(){
        if(UserInfoUtils.isLogin(this)){

        }else {
            StartUtils.startLogin(this
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this
                            , mAvatar, getString(R.string.avatar_transition_name)));
        }
    }

    @Click(R.id.settings)
    void onSettings(){
        StartUtils.startSettings(this);
    }

    @Click(R.id.fab)
    void onFab(){
        if(UserInfoUtils.isLogin(this)){
            StartUtils.startUpload(this);
        }else {
            StartUtils.startLogin(this
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this
                            , mAvatar, getString(R.string.avatar_transition_name)));
        }
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
