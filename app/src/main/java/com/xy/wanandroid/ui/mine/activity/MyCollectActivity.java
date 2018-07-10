package com.xy.wanandroid.ui.mine.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xy.wanandroid.R;
import com.xy.wanandroid.base.activity.BaseRootActivity;
import com.xy.wanandroid.contract.CollectContract;
import com.xy.wanandroid.data.mine.CollectBean;
import com.xy.wanandroid.model.constant.Constant;
import com.xy.wanandroid.presenter.mine.CollectPresenter;
import com.xy.wanandroid.ui.main.activity.ArticleDetailsActivity;
import com.xy.wanandroid.ui.mine.adapter.CollectAdapter;
import com.xy.wanandroid.util.app.LogUtil;
import com.xy.wanandroid.util.app.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyCollectActivity extends BaseRootActivity implements CollectContract.View, CollectAdapter.OnItemClickListener, CollectAdapter.OnItemChildClickListener {

    @BindView(R.id.normal_view)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.toolbar_collect)
    Toolbar mToolBarCollect;

    private List<CollectBean.DatasBean> collectList;
    private CollectAdapter mAdapter;
    private CollectPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setSupportActionBar(mToolBarCollect);
        getSupportActionBar().setTitle(getString(R.string.my_collect));
    }

    @Override
    protected void initUI() {
        super.initUI();
        showLoading();
        mRv.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    protected void initData() {
        setRefresh();
        collectList = new ArrayList<>();
        presenter = new CollectPresenter(this);
        presenter.getCollectList(0);
        mAdapter = new CollectAdapter(R.layout.item_homepage, collectList);
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mRv.setAdapter(mAdapter);
    }

    @Override
    public void getCollectListOk(CollectBean dataBean, boolean isRefresh) {
        LogUtil.e("getCollectListOk");
        if (mAdapter == null) {
            return;
        }
        if (isRefresh) {
            collectList = dataBean.getDatas();
            mAdapter.replaceData(dataBean.getDatas());
        } else {
            collectList.addAll(dataBean.getDatas());
            mAdapter.addData(dataBean.getDatas());
        }
        showNormal();
    }

    @Override
    public void getCollectListErr(String info) {
        showError();
    }

//    @Override
//    public void cancelCollectOk(String info) {
//        ToastUtil.show(activity, getString(R.string.cancel_collect_success));
//
//    }
//
//    @Override
//    public void cancelCollectErr(String info) {
//        ToastUtil.show(activity, info);
//    }

    @Override
    public void reload() {
        showLoading();
        presenter.getCollectList(0);
    }

    /**
     * SmartRefreshLayout刷新加载
     */
    private void setRefresh() {
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            presenter.autoRefresh();
            refreshLayout.finishRefresh(1000);
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            presenter.loadMore();
            refreshLayout.finishLoadMore(1000);
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.ARTICLE_TITLE, mAdapter.getData().get(position).getTitle());
        bundle.putString(Constant.ARTICLE_LINK, mAdapter.getData().get(position).getLink());
        bundle.putInt(Constant.ARTICLE_ID, mAdapter.getData().get(position).getId());
        bundle.putBoolean(Constant.ARTICLE_IS_COLLECT, Constant.TRUE);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, view, getString(R.string.share_view));
        startActivity(new Intent(activity, ArticleDetailsActivity.class).putExtras(bundle), options.toBundle());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//        presenter.cancelCollect(mAdapter.getData().get(position).getId());
    }
}
