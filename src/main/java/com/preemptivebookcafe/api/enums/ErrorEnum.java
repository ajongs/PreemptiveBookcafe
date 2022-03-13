package com.preemptivebookcafe.api.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    //signUp 관련 0으로 시작
    ID_ALREADY_EXISTS(001, "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);

    private Integer errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    ErrorEnum(int errorCode, String errorMessage, HttpStatus httpStatus){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
