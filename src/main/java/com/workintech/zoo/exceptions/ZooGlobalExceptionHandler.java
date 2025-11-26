package com.workintech.zoo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ZooGlobalExceptionHandler {

    @ExceptionHandler(ZooException.class)
    public ResponseEntity<ZooErrorResponse> handleZooException(ZooException ex) {
        log.error("ZooException:", ex);

        ZooErrorResponse body = new ZooErrorResponse(
                ex.getHttpStatus().value(),
                ex.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ZooErrorResponse> handleException(Exception ex) {
        log.error("Generic Exception:", ex);

        String msg = (ex.getMessage() == null || ex.getMessage().isBlank())
                ? "Bad Request"
                : ex.getMessage();

        ZooErrorResponse body = new ZooErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                msg,
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
