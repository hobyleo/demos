package com.hoby.exception;

/**
 * 限流异常
 *
 * @author hoby
 * @since 2023-12-27
 */
public class RateLimitException extends RuntimeException {

    public RateLimitException() {
    }

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }

}
