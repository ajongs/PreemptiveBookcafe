package com.preemptivebookcafe.api.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {
    //signUp 관련 0으로 시작
    ID_ALREADY_EXISTS(001, "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
    INVALID_CLASS_NO(002, "유효하지 않은 학번입니다.", HttpStatus.BAD_REQUEST),
    //user 관련
    INVALID_PASSWORD(100, "비밀번호가 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_HAS_SEAT(101, "사용중인 좌석이 없습니다.", HttpStatus.BAD_REQUEST),

    SEAT_ALREADY_USED(300, "좌석이 사용중입니다.", HttpStatus.BAD_REQUEST),
    DO_NOT_REPORT(301, "빈좌석이거나 이미 신고된 좌석은 신고할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_SEAT(302, "존재하지 않는 좌석번호 입니다.", HttpStatus.BAD_REQUEST),
    SEAT_INSUFFICIENT_TIME(303, "해당 좌석 사용시간이 얼마 남지 않아 신고할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_HAS_SEAT(304, "이미 사용중인 좌석이 있습니다.",HttpStatus.BAD_REQUEST),
    INCONSISTENCY_SEAT_ID(305, "요청하신 좌석 번호와 사용중인 좌석 번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    //jwt validation
    NULL_TOKEN(900, "JWT 토큰이 비었습니다. 확인해주십시오.", HttpStatus.BAD_REQUEST),
    NO_USER_IN_TOKEN(999, "유효하지 않은 학번입니다.", HttpStatus.BAD_REQUEST),
    INVALID_ACCESS_TOKEN(901, "유효하지 않은 Access Token 입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_ACCESS_TOKEN(902, "만료된 Access Token 입니다.", HttpStatus.BAD_REQUEST ),
    MALFORMED_ACCESS_TOKEN(903, "Access Token이 올바른 JWT 구조가 아닙니다.", HttpStatus.BAD_REQUEST ),
    UNSUPPORTED_ACCESS_TOKEN(904, "이 서버에서 만들어진 Access Token이 아닙니다.", HttpStatus.BAD_REQUEST ),
    NOT_REFRESH_TOKEN(905, "토큰 재발급시에는 RefreshToken을 서버로 전송하여야 합니다. ", HttpStatus.BAD_REQUEST),

    FLAG_INVALID(911, "토큰과 플래그가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_IN_THIS_TOKEN(912, "토큰의 id에 해당하는 유저가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),

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
