package com.xy.wanandroid.model.api;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class HttpObserver<T> implements Observer<T> {

    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e){

    }

    public abstract void onErrorInfo(T t);
}
