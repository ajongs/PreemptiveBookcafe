package com.preemptivebookcafe.api.dto.user;

import lombok.Data;

@Data
public class UserLogoutResponseDto {
    private Long classNo;
    private String fireToken;
}
