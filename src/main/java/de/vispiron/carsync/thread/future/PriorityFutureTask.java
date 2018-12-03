package de.vispiron.carsync.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author karamim
 */
public class PriorityFutureTask<T> extends FutureTask<T> {

    private int priority;

    public PriorityFutureTask(Callable<T> callable, int priority) {
        super(callable);
        this.priority = priority;
    }

    public PriorityFutureTask(Runnable runnable, T result, int priority) {
        super(runnable, result);
        this.priority = priority;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        super.run();
    }

    @Override
    public String toString() {
        return "Priority:" + this.priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int toPriority) {
        this.priority = toPriority;
    }
}
