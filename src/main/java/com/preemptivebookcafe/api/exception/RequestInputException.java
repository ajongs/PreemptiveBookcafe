package com.preemptivebookcafe.api.exception;

import com.preemptivebookcafe.api.enums.ErrorEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RequestInputException extends DefaultException{

    public RequestInputException(ErrorEnum errorEnum){
        super(errorEnum);
    }
    public RequestInputException(String className, ErrorEnum errorEnum){
        super(className, errorEnum);
    }
}
