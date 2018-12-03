package com.metao.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

public class ServiceViewImp<T> implements ServiceView<T> {

    @Autowired
    JobExecutor jobExecutor;

    ServiceViewImp() {
        //jobExecutor.deliverJob(this, new JobModel());
    }


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
