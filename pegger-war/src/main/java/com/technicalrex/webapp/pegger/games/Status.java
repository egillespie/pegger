package com.technicalrex.webapp.pegger.games;

import com.google.common.base.Preconditions;

public class Status {
    private final String message;

    public Status(String message) {
        this.message = Preconditions.checkNotNull(message);
    }

    public String getMessage() {
        return message;
    }
}
