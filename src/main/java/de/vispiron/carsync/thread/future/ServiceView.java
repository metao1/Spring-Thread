package de.vispiron.carsync.thread.future;

import org.springframework.stereotype.Component;

@Component
public interface ServiceView<T> {

    public void onNext(T t);

    public void onError(Throwable e);

    public void onComplete();
}
