package com.preemptivebookcafe.api.service.user;

import com.preemptivebookcafe.api.dto.user.KioskUserResponseDto;
import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;

import java.util.Map;

public interface UserService {
    public UserResponseDto signUp(UserRequestDto requestDto);
    public Map login(UserRequestDto requestDto);
    Long getLoginClassNo();
    User getUser();

    KioskUserResponseDto kioskLogin(UserRequestDto requestDto);
}
