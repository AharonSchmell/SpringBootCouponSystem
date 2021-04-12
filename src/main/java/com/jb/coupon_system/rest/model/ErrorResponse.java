package com.jb.coupon_system.rest.model;

/**
 * A helper class that provides the ControllerAdvice class a user friendly response
 * for exceptions that are thrown throughout the program, using a simple message and timestamp.
 */
public class ErrorResponse {
    private final String message;
    private final long timestamp;

    public ErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public static ErrorResponse ofNow(String message) {
        return new ErrorResponse(message, System.currentTimeMillis());
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}