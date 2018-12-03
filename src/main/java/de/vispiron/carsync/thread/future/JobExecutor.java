package de.vispiron.carsync.thread.future;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Stream;

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
                throw new JobExecutorException(Utils.format("unable to run the job:", e.getMessage()));
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

        JobAnalyzer(Object jobInterface) {
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
                    //create Thread pool as our threads number we want to run
                    final PriorityExecutorService executorService =
                            ExecutorsHelper.newPriorityFixedThreadPool(schedulerMap.size());
                    startAnalyzing(executorService);
                }
            });
        }

        void startAnalyzing(PriorityExecutorService executorService) {
            objectStream = schedulerMap.entrySet().parallelStream()
                    .map(Map.Entry::getValue);
            objectStream.forEach(jobModel -> executorService.submit(() -> {
                try {
                    jobModel.getMethod().invoke(jobModel.getJobInterface());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }, jobModel.getPriority()));
        }
    }
}
