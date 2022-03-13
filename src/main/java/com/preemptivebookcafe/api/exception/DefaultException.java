package com.preemptivebookcafe.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonIgnoreProperties({"cause", "stackTrace", "message", "localizedMessage", "suppressed"})
public class DefaultException extends RuntimeException{
    private String className;
    private Integer errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;
    private String detailTrace;

    DefaultException(String className, ErrorEnum errorEnum){
        this.className = className;
        errorCode = errorEnum.getErrorCode();
        errorMessage = errorEnum.getErrorMessage();
        httpStatus = errorEnum.getHttpStatus();
    }
    DefaultException(ErrorEnum errorEnum){
        this.className = this.getClass().getSimpleName();
        errorCode = errorEnum.getErrorCode();
        errorMessage = errorEnum.getErrorMessage();
        httpStatus = errorEnum.getHttpStatus();
    }
}
