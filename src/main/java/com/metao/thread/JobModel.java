package com.metao.thread;

import java.lang.reflect.Method;
import java.util.UUID;

public class JobModel{

    private final String id;
    private final String name;
    private final int priority;
    private final Method method;
    private StatusType status;
    private final Object jobInterface;

    public JobModel(String name, int priority, Method method, Object jobInterface) {
        this.id = UUID.randomUUID().toString().substring(0, 5).replace("-", "");
        this.name = name;
        this.priority = priority;
        this.jobInterface = jobInterface;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public Object getJobInterface() {
        return jobInterface;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "JobModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';
    }

}
