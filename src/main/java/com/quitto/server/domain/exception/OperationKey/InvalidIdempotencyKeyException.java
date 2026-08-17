package com.quitto.server.domain.exception;

/**
 * Domain exception for invalid idempotency keys.
 * Used when an idempotency key fails validation checks (null/blank ID or value,
 * or not found in cache during validation).
 */
public class InvalidIdempotencyKeyException extends RuntimeException {

    public InvalidIdempotencyKeyException() {
        super();
    }

    public InvalidIdempotencyKeyException(String message) {
        super(message);
    }

    public InvalidIdempotencyKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}