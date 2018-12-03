package com.metao.thread;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.metao.thread.Utils.format;

@Service("JobExecutor")
public class JobExecutor {

    private final Object object;
    private final IdentityHashMap<String, JobModel> schedulerMap;

    public JobExecutor() {
        schedulerMap = new IdentityHashMap<>();
        object = new Object();
    }

    public void startJobs(Object jobInterface) {
        synchronized (object) {
            try {
                new JobAnalyzer(jobInterface);
            } catch (Exception e) {
                throw new JobExecutorException(format("unable to run the job:", e.getMessage()));
            }
        }
    }

    /*private class RxHandler {

        RxHandler(Object jobInterface) {

        }
        peopleObserver
                .observeOn(scheduler)
                .subscribe(new FlowableSubscriber<JobModel>() {

            @Override
            public void onSubscribe(Subscription s) {

            }

            @Override
            public void onNext(JobModel job) {
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });
    }*/

    private class JobAnalyzer {
        private Stream<JobModel> objectStream;
        private Stream<String> keyStream;

        public JobAnalyzer(Object jobInterface) {
            Scheduler scheduler = Schedulers.newThread();
            Scheduler.Worker workerThread = scheduler.createWorker();
            workerThread.schedule(() -> {
                Class<?> aClass = jobInterface.getClass();
                Method[] declaredAnnotationsByJob = aClass.getDeclaredMethods();
                for (Method method : declaredAnnotationsByJob) {
                    method.setAccessible(true);
                    if (method.isAnnotationPresent(Job.class)) {
                        Job annotation = method.getAnnotation(Job.class);
                        String name = annotation.name();
                        int priority = annotation.priority();
                        JobModel jobModel = new JobModel(name, priority, method, jobInterface);
                        schedulerMap.put(name, jobModel);
                    }
                }
                if (!schedulerMap.isEmpty()) {
                    startAnalyzing();
                }
            });
        }

        void startAnalyzing() {
            AtomicInteger maxPriority = new AtomicInteger();
            objectStream = schedulerMap.entrySet().parallelStream()
                    .map(Map.Entry::getValue);
            keyStream = schedulerMap.entrySet().parallelStream()
                    .map(Map.Entry::getKey);
            objectStream = objectStream.sorted((o1, o2) -> o1.getPriority() > o2.getPriority() ? o1.getPriority()
                    : o2.getPriority());
            objectStream.forEach(action -> {
                if (action.getStatus() != StatusType.QUEUED) {
                    maxPriority.set(action.getPriority());
                }
            });
        }
    }
}
