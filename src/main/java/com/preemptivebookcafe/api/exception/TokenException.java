package com.preemptivebookcafe.api.exception;

import com.preemptivebookcafe.api.enums.ErrorEnum;

public class TokenException extends DefaultException{

    public TokenException(String className, ErrorEnum errorEnum) {
        super(className, errorEnum);
    }

    public TokenException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
