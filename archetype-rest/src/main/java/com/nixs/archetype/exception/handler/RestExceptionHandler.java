package com.nixs.archetype.exception.handler;

import com.nixs.archetype.exception.ItemAlreadyExistsException;
import com.nixs.archetype.exception.ItemNotFoundException;
import java.util.UUID;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<RestErrorResponse> itemNotFoundHandler(ItemNotFoundException exception) {
        return handleError(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<RestErrorResponse> itemAlreadyExistsExceptionHandler(
        ItemAlreadyExistsException exception) {
        return handleError(HttpStatus.CONFLICT, exception);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestErrorResponse> accessDeniedExceptionHandler() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorResponse> defaultExceptionHandler(Exception exception) {
        return handleError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    private ResponseEntity<RestErrorResponse> handleError(HttpStatus status, Exception e) {
        String incidentId = UUID.randomUUID().toString();
        RestErrorResponse errorResponse = RestErrorResponse.builder()
            .incidentId(incidentId)
            .build();
        log.error("{} occurred incidentId={}: {}", e.getClass(), incidentId, e.getMessage(), e);
        return ResponseEntity.status(status.value()).body(errorResponse);
    }

    @Builder
    private record RestErrorResponse(String incidentId) {

    }

}
