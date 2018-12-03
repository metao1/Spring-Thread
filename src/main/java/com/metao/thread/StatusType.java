package com.metao.thread;

import javax.validation.constraints.NotNull;

public enum StatusType {
    QUEUED("queued"),
    SENDING("sending"),
    COMPLETED("completed"),
    INVALID("invalid"),
    FAILED("failed");

    @NotNull
    private final String type;

    StatusType(@NotNull String type) {
        this.type = type;
    }

    @NotNull
    static StatusType fromString(String statusTypeString) {
        switch (statusTypeString) {
            case "queued":
                return QUEUED;
            case "sending":
                return SENDING;
            case "completed":
                return COMPLETED;
            case "failed":
                return FAILED;
            case "invalid":
            default: {
                return INVALID;
            }
        }
    }

    @NotNull
    @Override
    public String toString() {
        return type;
    }

}
