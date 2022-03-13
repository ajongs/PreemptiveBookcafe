package com.preemptivebookcafe.api.dto.user;

import lombok.Data;

@Data //@Getter, @Setter, @RequiredArgsConstructor, @ToString 등을 합쳐놓은 것
public class UserResponseDto {
    private Long classNo;
    private String email;
}
