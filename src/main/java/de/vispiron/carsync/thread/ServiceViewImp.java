package de.vispiron.carsync.thread;

import de.vispiron.carsync.thread.future.JobExecutor;
import de.vispiron.carsync.thread.future.ServiceView;
import org.springframework.beans.factory.annotation.Autowired;

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
