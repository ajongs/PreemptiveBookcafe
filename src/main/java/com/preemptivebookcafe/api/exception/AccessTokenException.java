package com.preemptivebookcafe.api.exception;

import com.preemptivebookcafe.api.enums.ErrorEnum;

public class AccessTokenException extends DefaultException{

    public AccessTokenException(String className, ErrorEnum errorEnum) {
        super(className, errorEnum);
    }

    public AccessTokenException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
