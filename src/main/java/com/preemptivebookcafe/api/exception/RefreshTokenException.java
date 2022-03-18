package com.preemptivebookcafe.api.exception;

import com.preemptivebookcafe.api.enums.ErrorEnum;

public class RefreshTokenException extends DefaultException{

    public RefreshTokenException(String className, ErrorEnum errorEnum) {
        super(className, errorEnum);
    }

    public RefreshTokenException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
