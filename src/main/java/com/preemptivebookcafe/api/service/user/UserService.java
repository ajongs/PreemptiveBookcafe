package com.preemptivebookcafe.api.service.user;

import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;

import java.util.Map;

public interface UserService {
    public UserResponseDto signUp(UserRequestDto requestDto);
    public Map login(UserRequestDto requestDto);
}
