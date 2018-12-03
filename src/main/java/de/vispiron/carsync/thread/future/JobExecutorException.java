package de.vispiron.carsync.thread.future;

public class JobExecutorException extends RuntimeException {

    private  final String message;

    public JobExecutorException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
