package com.preemptivebookcafe.api.service.user;

import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;

public interface UserService {
    public UserResponseDto signUp(UserRequestDto requestDto);
}
