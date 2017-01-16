package com.x.vscam.global.ui;

import java.util.List;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DimensionPixelSizeRes;

import com.x.vscam.R;
import com.x.vscam.global.net.ApiIml;
import com.x.vscam.global.net.ApiInterface;
import com.x.vscam.global.utils.ProcessDataUtils;
import com.x.vscam.main.ImgFlowBean;
import com.x.vscam.main.ImgFlowItem_;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import ykooze.ayaseruri.codesslib.adapter.RecyclerAdapter;
import ykooze.ayaseruri.codesslib.io.SerializeUtils;
import ykooze.ayaseruri.codesslib.others.ServerException;
import ykooze.ayaseruri.codesslib.ui.CommonRecyclerView;
import ykooze.ayaseruri.codesslib.ui.GridSpacingItemDecoration;

/**
 * Created by wufeiyang on 2017/1/16.
 */
@EViewGroup(R.layout.view_img_flow)
public class ImgFlowView extends SwipeRefreshLayout {

    private static final short SPAN_COUNT = 2;
    private static final short PAGE_SIZE = 30;
    private static final short PRE_LOADING_PADDING = 20;//小于PAGE_SIZE的任意一个值

    @ViewById(R.id.recycler)
    CommonRecyclerView mRecyclerView;
    @DimensionPixelSizeRes(R.dimen.img_flow_spacing)
    int mImgFlowSpacing;

    private int mLastUnix;

    public ImgFlowView(Context context) {
        super(context);
    }

    public ImgFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(final String tag, final int uid){
        final ApiInterface apiInterface = ApiIml.getInstance(getContext());

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
                return  (List<ImgFlowBean.GridsBean>) SerializeUtils.deserializationSync(getContext(), tag, true);
            }

            @Override
            public List<ImgFlowBean.GridsBean> getRefreshData() throws Exception {
                ImgFlowBean data = apiInterface.getImgFlow(0, uid).execute().body();
                data = ProcessDataUtils.addUserInfo(data);
                SerializeUtils.serializationSync(getContext(), tag, data.getGrids());
                return data.getGrids();
            }

            @Override
            public List<ImgFlowBean.GridsBean> getLoadMoreData() throws Exception {
                ImgFlowBean data = apiInterface.getImgFlow(mLastUnix, uid).execute().body();
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
                setRefreshing(true);
            }

            @Override
            public void uiLoadingError(@CommonRecyclerView.LoadingType int loadingType, Throwable e) {
                setRefreshing(false);
                if(e instanceof ServerException){
                    Snackbar.make(getRootView(), R.string.data_error, Snackbar.LENGTH_LONG).show();
                }else {
                    Snackbar.make(getRootView(), R.string.net_error, Snackbar.LENGTH_LONG).show();
                }
                e.printStackTrace();
            }

            @Override
            public void uiLoadingComplete(@CommonRecyclerView.LoadingType int loadingType) {
                setRefreshing(false);
                if(CommonRecyclerView.TYPE_END == loadingType){
                    Snackbar.make(getRootView(), R.string.already_end, Snackbar.LENGTH_LONG).show();
                }
            }
        }, new RecyclerAdapter<ImgFlowBean.GridsBean>() {
            @Override
            protected Item onCreateItemView(ViewGroup parent, int viewType) {
                return ImgFlowItem_.build(parent.getContext());
            }
        });

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.refreshData();
            }
        });

        mRecyclerView.refreshData();
    }
}
