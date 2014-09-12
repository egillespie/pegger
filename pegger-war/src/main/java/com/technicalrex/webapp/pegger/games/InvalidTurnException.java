package com.technicalrex.webapp.pegger.games;

public class InvalidTurnException extends RuntimeException {
    private final Status status;

    public InvalidTurnException(String message) {
        this(new Status(message));
    }

    public InvalidTurnException(Status status) {
        super(status.getMessage());
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
