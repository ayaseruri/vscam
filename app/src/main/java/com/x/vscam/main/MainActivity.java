package com.x.vscam.main;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.x.vscam.R;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.net.ApiInterface;
import com.x.vscam.global.ui.BaseActivity;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.global.utils.StartUtils;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import ykooze.ayaseruri.codesslib.adapter.RecyclerAdapter;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;
import ykooze.ayaseruri.codesslib.others.ServerException;
import ykooze.ayaseruri.codesslib.ui.CommonRecyclerView;
import ykooze.ayaseruri.codesslib.ui.GridSpacingItemDecoration;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private static final short SPAN_COUNT = 2;
    private static final short PAGE_SIZE = 20;

    @ViewById(R.id.activity_main)
    View mRoot;
    @ViewById(R.id.recycler)
    CommonRecyclerView mRecyclerView;
    @ViewById(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @DimensionPixelSizeRes(R.dimen.img_flow_spacing)
    int mImgFlowSpacing;

    private ApiInterface mApiInterface;
    private int mLastUnix;

    @AfterViews
    void init(){
        mApiInterface = ApiIml.getInstance(MainActivity.this);

        mRecyclerView.setPreLoadPadding(0);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(SPAN_COUNT
                , mImgFlowSpacing
                , true
                , false
                , false));
        mRecyclerView.init(new CommonRecyclerView.ICommonRecyclerView<ImgFlowBean.GridsBean>() {
            @Override
            public List<ImgFlowBean.GridsBean> getFirstInData() throws Exception {
                ImgFlowBean imgFlowBean = SerializeUtils.deserializationSync(MainActivity.this, ImgFlowBean
                        .class, true);
                return null == imgFlowBean ? null : imgFlowBean.getGrids();
            }

            @Override
            public List<ImgFlowBean.GridsBean> getRefreshData() throws Exception {
                ImgFlowBean data = mApiInterface.getImgFlow(0).execute().body();
                data = ProcessDataUtils.addUserInfo(data);
                SerializeUtils.serializationSync(MainActivity.this, data.getGrids());
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

    @Click(R.id.fab)
    void onFab(){
        StartUtils.startUpload(this);
    }
}
