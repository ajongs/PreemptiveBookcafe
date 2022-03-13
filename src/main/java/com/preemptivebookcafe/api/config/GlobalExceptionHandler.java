package com.preemptivebookcafe.api.config;

import com.preemptivebookcafe.api.exception.DefaultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<DefaultException> defaultException(Throwable e){
        logger.error("defaultException", e);
        DefaultException defaultException = null;
        if(e instanceof DefaultException){
            defaultException = (DefaultException) e;
            defaultException.setDetailTrace(e.getStackTrace()[0].toString());
        }

        return ResponseEntity.badRequest().body(defaultException);
    }

}
