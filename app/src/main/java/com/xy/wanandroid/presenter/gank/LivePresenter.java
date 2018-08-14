package com.xy.wanandroid.presenter.gank;

import com.xy.wanandroid.base.presenter.BasePresenter;
import com.xy.wanandroid.contract.gank.LiveContract;
import com.xy.wanandroid.data.gank.LiveUrl;
import com.xy.wanandroid.model.api.ApiService;
import com.xy.wanandroid.model.api.ApiStore;
import com.xy.wanandroid.model.api.BaseResp;
import com.xy.wanandroid.model.api.HttpObserver;
import com.xy.wanandroid.model.constant.Constant;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jxy on 2018/7/23.
 */

public class LivePresenter extends BasePresenter<LiveContract.View> implements LiveContract.Presenter {

    @Inject
    public LivePresenter() {

    }

    @Override
    public void getLiveUrl(String roomId) {
        ApiStore.createApi(ApiService.class)
                .getLiveUrl(roomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpObserver<BaseResp<LiveUrl>>() {
                    @Override
                    public void onNext(BaseResp<LiveUrl> baseResp) {
                        if (baseResp.getError() == Constant.REQUEST_SUCCESS) {
                            if (mView != null)
                                mView.getLiveUrlOk(baseResp.data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.getLiveUrlErr(e.getMessage());
                    }
                });
    }

}
