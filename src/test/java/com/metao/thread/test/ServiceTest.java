package com.metao.thread.test;

import com.metao.thread.PeopleService;
import com.metao.thread.Person;
import com.metao.thread.ServiceView;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    PeopleService peopleService;

    Flowable<Person> peopleObserver;

    @Autowired
    ServiceView<Person> serviceView;

    @Test
    public void testPeopleService() {
        /*
        Scheduler scheduler = Schedulers.newThread();
        Scheduler.Worker workerThread = scheduler.createWorker();
        workerThread.schedule(()-> {
        });
        */
        assertNotNull(peopleObserver);
        /*TestSubscriber<Person> personTestObserver = peopleObserver
                .observeOn(Schedulers.newThread()).test();
        personTestObserver
                .awaitTerminalEvent();
        personTestObserver
                .assertNoErrors()
                .assertValue(Objects::nonNull);*/

        peopleObserver
                .observeOn(Schedulers.newThread())
                .subscribe(new FlowableSubscriber<Person>() {
                    @Override
                    public void onSubscribe(Subscription s) {

                    }

                    @Override
                    public void onNext(Person person) {
                        assertNotNull(person);
                        assertNotNull(person.getName());
                        assertTrue(person.getName().length() > 0);
                        serviceView.onNext(person);
                    }

                    @Override
                    public void onError(Throwable e) {
                        serviceView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        serviceView.onComplete();
                    }
                });

    }


    @Before
    public void beforeTest() {
        peopleObserver = peopleService.getPeopleObserver();
    }

}
