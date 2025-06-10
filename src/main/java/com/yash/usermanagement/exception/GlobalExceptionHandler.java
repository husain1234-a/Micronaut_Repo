package com.yash.usermanagement.exception;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;

@Singleton
@Produces
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<ErrorResponse>> {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public HttpResponse<ErrorResponse> handle(HttpRequest request, Exception exception) {
        LOG.error("Error occurred while processing request: {}", request.getPath(), exception);

        if (exception instanceof ResourceNotFoundException) {
            LOG.warn("Resource not found: {}", exception.getMessage());
            return HttpResponse.notFound(new ErrorResponse(404, exception.getMessage()));
        } else if (exception instanceof ConstraintViolationException) {
            LOG.warn("Validation error: {}", exception.getMessage());
            return HttpResponse.badRequest(new ErrorResponse(400, "Validation error: " + exception.getMessage()));
        } else if (exception instanceof DatabaseException) {
            LOG.error("Database error: {}", exception.getMessage(), exception);
            return HttpResponse.serverError(new ErrorResponse(500, "Database error: " + exception.getMessage()));
        } else if (exception instanceof SQLException) {
            LOG.error("SQL error: {}", exception.getMessage(), exception);
            return HttpResponse.serverError(new ErrorResponse(500, "Database error occurred"));
        } else {
            LOG.error("Unexpected error: {}", exception.getMessage(), exception);
            return HttpResponse.serverError(new ErrorResponse(500, "Internal server error"));
        }
    }
}

class ErrorResponse {
    private int status;
    private String message;
    private String timestamp;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = java.time.LocalDateTime.now().toString();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}