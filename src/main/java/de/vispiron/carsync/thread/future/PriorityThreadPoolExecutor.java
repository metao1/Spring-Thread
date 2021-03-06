package de.vispiron.carsync.thread.future;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.concurrent.*;

/**
 * @author karamim
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor implements PriorityExecutorService {

    private final BlockingDeque<Runnable> workQueue;

    private static final RejectedExecutionHandler defaultHandler = new ThreadPoolExecutor.AbortPolicy();

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), defaultHandler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, threadFactory, defaultHandler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, Executors.defaultThreadFactory(), handler);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingDeque<Runnable>(corePoolSize, new PriorityFutureTaskComparator()), threadFactory, handler);
        this.workQueue = (BlockingDeque<Runnable>) super.getQueue();
    }

    public Future<?> submit(Runnable task) {
        return this.submit(task, Thread.NORM_PRIORITY);
    }

    public <T> Future<T> submit(@NotNull Runnable task, T result) {
        return this.submit(task, result, Thread.NORM_PRIORITY);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.submit(task, Thread.NORM_PRIORITY);
    }

    public Future<?> submit(Runnable runnable, int priority) {
        if (runnable == null)
            throw new NullPointerException();
        RunnableFuture<Object> task = newPriorityTaskFor(runnable, null, priority);
        execute(task);
        return task;
    }

    public <T> Future<T> submit(Runnable runnable, T result, int priority) {
        if (runnable == null)
            throw new NullPointerException();
        RunnableFuture<T> task = newPriorityTaskFor(runnable, result, priority);
        execute(task);
        return task;
    }

    public <T> Future<T> submit(Callable<T> callable, int priority) {
        if (callable == null)
            throw new NullPointerException();
        RunnableFuture<T> task = newPriorityTaskFor(callable, priority);
        execute(task);
        return task;
    }

    protected <T> RunnableFuture<T> newPriorityTaskFor(Runnable runnable, T value, int priority) {
        return new PriorityFutureTask<T>(runnable, value, priority);
    }

    protected <T> RunnableFuture<T> newPriorityTaskFor(Callable<T> callable, int priority) {
        return new PriorityFutureTask<T>(callable, priority);
    }

    @SuppressWarnings("rawtypes")
    public int getLeastPriority() {
        PriorityFutureTask task = ((PriorityFutureTask) this.workQueue.peekLast());
        return task != null ? task.getPriority() : Integer.MIN_VALUE;
    }

    @SuppressWarnings("rawtypes")
    public int getHighestPriority() {
        PriorityFutureTask task = ((PriorityFutureTask) this.workQueue.peekFirst());
        return task != null ? task.getPriority() : Integer.MAX_VALUE;
    }

    /**
     * makes best possible effort to change priorities but does not guarantee
     * the same.
     */
    @Override
    public void changePriorities(int fromPriority, int toPriority) {
        PriorityFutureTask<?>[] tasks = this.workQueue.toArray(new PriorityFutureTask<?>[0]);

        for (PriorityFutureTask<?> task : tasks) {
            if (task.getPriority() == fromPriority)
                if (this.workQueue.remove(task)) {
                    task.setPriority(toPriority);
                    this.workQueue.offer(task);
                }
        }
    }

    @SuppressWarnings("rawtypes")
    private static class PriorityFutureTaskComparator<T extends PriorityFutureTask> implements Comparator<T> {
        @Override
        public int compare(T t1, T t2) {
            if (t1 == null && t2 == null)
                return 0;
            else if (t1 == null)
                return -1;
            else if (t2 == null)
                return 1;
            else {
                int p1 = t1.getPriority();
                int p2 = t2.getPriority();

                return Integer.compare(p1, p2);
            }
        }
    }
}
