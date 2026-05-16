package gr.athtech.app.bookmanager.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {
    private final Long retryAfterSeconds;

    public RateLimitException(Long retryAfterSeconds) {
        super("No API request quota available");
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
