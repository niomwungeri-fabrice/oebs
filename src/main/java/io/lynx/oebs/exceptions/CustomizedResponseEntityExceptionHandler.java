package io.lynx.oebs.exceptions;

import io.lynx.oebs.dtos.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> authExceptions(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        switch (ex.getMessage()) {
            case "Bad credentials" -> status = HttpStatus.UNAUTHORIZED;
            case "Forbidden" -> status = HttpStatus.FORBIDDEN;
            case "email address already in use." -> status = HttpStatus.BAD_REQUEST;
            default -> log.error("unhandled error {}", ex.getMessage());
        }
        return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> resourceNotFound(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public final ResponseEntity<Object> resourceWithConflict(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceUnAuthorizedException.class)
    public final ResponseEntity<Object> accountNotVerified(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalServerException.class)
    public final ResponseEntity<Object> internalServerError(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(ErrorResponse.builder()
                .error(ex.getMessage())
                .build());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}