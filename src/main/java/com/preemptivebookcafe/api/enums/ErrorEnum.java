package com.preemptivebookcafe.api.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    //signUp 관련 0으로 시작
    ID_ALREADY_EXISTS(001, "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),


    //jwt validation
    NULL_TOKEN(900, "JWT 토큰이 비었습니다. 확인해주십시오.", HttpStatus.BAD_REQUEST),
    NO_USER_IN_TOKEN(999, "해당 유저가 없거나 탈퇴한 유저입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN(901, "유효하지 않은 Access Token 입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_ACCESS_TOKEN(902, "만료된 Access Token 입니다.", HttpStatus.BAD_REQUEST ),
    MALFORMED_ACCESS_TOKEN(903, "Access Token이 올바른 JWT 구조가 아닙니다.", HttpStatus.BAD_REQUEST ),
    UNSUPPORTED_ACCESS_TOKEN(904, "이 서버에서 만들어진 Access Token이 아닙니다.", HttpStatus.BAD_REQUEST ),

    FLAG_INVALID(911, "토큰과 플래그가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_REFRESH_TOKEN(921, "유효하지 않은 Refresh Token 입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_REFRESH_TOKEN(922, "만료된 Refresh Token 입니다.", HttpStatus.BAD_REQUEST ),
    MALFORMED_REFRESH_TOKEN(923, "Refresh Token이 올바른 JWT 구조가 아닙니다.", HttpStatus.BAD_REQUEST ),
    UNSUPPORTED_REFRESH_TOKEN(924, "이 서버에서 만들어진 Refresh Token이 아닙니다.", HttpStatus.BAD_REQUEST );


    private Integer errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    ErrorEnum(int errorCode, String errorMessage, HttpStatus httpStatus){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
