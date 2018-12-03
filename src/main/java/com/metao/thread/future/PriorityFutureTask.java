package com.metao.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityFutureTask<T> extends FutureTask<T> {

    private final int priority;

    public PriorityFutureTask(Callable<T> callable, int priority) {
        super(callable);
        this.validatePriority(priority);
        this.priority = priority;
    }

    private void validatePriority(int priority) {
        if (priority > Thread.MAX_PRIORITY || priority < Thread.MIN_PRIORITY) {
            throw new IllegalArgumentException("priority should be between Thread.MAX_PRIORITY & Thread.MIN_PRIORITY");
        }
    }

    @Override
    public void run() {
        int originalPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(priority);
        super.run();
        Thread.currentThread().setPriority(originalPriority);
    }
}
