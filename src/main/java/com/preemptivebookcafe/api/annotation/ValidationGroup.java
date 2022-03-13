package com.preemptivebookcafe.api.annotation;

import javax.validation.groups.Default;

//javax.validation.groups.Default 에는 group 에 해당 마커 인터페이스가 적용되지 않은 validation 까지 유효성 검증한 내용을 가지고 있음.
//ex) @size(min =1, max=6) @NotEmpty(message="비면안됩니다.", groups = ValidationGroup.signUp.class)
//이때 size 의 유효성검증 결과도 default 로 부터 받아볼 수 있다.

public class ValidationGroup {
    public interface signUp extends Default{};
    public interface login extends Default{};
}
