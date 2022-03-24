package com.preemptivebookcafe.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //런타임에 실행되는 애노테이션
@Target(ElementType.METHOD) //메소드위에 사용가능한 애노테이션
public @interface Auth {
}
