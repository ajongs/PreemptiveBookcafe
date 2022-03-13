package com.preemptivebookcafe.api.controller.user;

import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody User user) {
        return ResponseEntity.ok()
                .body(userService.signUp(user));
    }

    @PostMapping("/user/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setClassNo(user.getClassNo());
        return ResponseEntity.ok().body(userResponseDto);
    }
}
