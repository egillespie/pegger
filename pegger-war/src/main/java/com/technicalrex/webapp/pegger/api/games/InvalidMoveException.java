package com.technicalrex.webapp.pegger.api.games;

public class InvalidMoveException extends RuntimeException {
    private final Reason reason;

    public InvalidMoveException(String message) {
        super(message);
        this.reason = new Reason(message);
    }

    public Reason getReason() {
        return reason;
    }

    public static class Reason {
        private final String message;

        private Reason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
