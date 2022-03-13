package com.preemptivebookcafe.api.dto.user;

import com.preemptivebookcafe.api.annotation.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserRequestDto {

    @Size(min=9, max=9)
    @NotBlank(message="학번을 입력해 주세요.", groups = {ValidationGroup.signUp.class})
    private Long classNo;


    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z0-9$@$!%*#?&]{8,15}$",
            message = "패스워드는 영문자,숫자,특수문자를 최소 1개씩 포함해야합니다.")
    @Size(min=8, max=15, message="비밀번호는 최소 8글자, 최대 15글자입니다.")
    @NotBlank(message = "패스워드를 입력해 주세요.", groups = {ValidationGroup.signUp.class})
    private String password;

    @NotBlank(message = "이메일을 입력해 주세요.", groups = {ValidationGroup.signUp.class})
    private String email;


}
