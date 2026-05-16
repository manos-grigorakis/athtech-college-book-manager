package gr.athtech.app.bookmanager.exception;

import gr.athtech.app.bookmanager.transfer.common.ApiResponse;
import gr.athtech.app.bookmanager.transfer.common.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Field Validation Error - 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> details = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(
                error -> details.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Field Validation Failed",
                details
        );

        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(), HttpStatus.BAD_REQUEST);
    }

    // Bad Credentials Error - 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(), HttpStatus.UNAUTHORIZED);
    }

    // Forbidden Exception Error - 403
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbiddenException(ForbiddenException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                null
        );

        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(), HttpStatus.FORBIDDEN);
    }

    // Resource Not Found Error - 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );

        log.error(ex.getMessage());
        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(), HttpStatus.NOT_FOUND);
    }

    // Conflict Exception Error - 409
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        );

        log.error(ex.getMessage());
        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(), HttpStatus.CONFLICT);
    }

    // Rate Limit Exception - 429
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimitException(RateLimitException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Rate-Limit-Retry-After-Seconds", String.valueOf(ex.getRetryAfterSeconds()));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS.value(),
                ex.getMessage(),
                null
        );

        log.error(ex.getMessage());
        return new ResponseEntity<>(
                ApiResponse.<Void>builder().error(errorResponse).build(), headers, HttpStatus.TOO_MANY_REQUESTS);
    }

    // Server Error - 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                null
        );

        log.error(ex.getMessage());
        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(),
                                    HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // External Client Exception - 503
    @ExceptionHandler(ExternalClientException.class)
    public ResponseEntity<ApiResponse<Void>> handleExternalClientException(ExternalClientException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ex.getMessage(),
                null
        );

        log.error(ex.getMessage());
        return new ResponseEntity<>(ApiResponse.<Void>builder().error(errorResponse).build(),
                                    HttpStatus.SERVICE_UNAVAILABLE);
    }
}
