package com.preemptivebookcafe.api.controller.user;

import com.preemptivebookcafe.api.annotation.Auth;
import com.preemptivebookcafe.api.dto.user.KioskUserResponseDto;
import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok()
                .body(userService.signUp(requestDto));
    }


    @PostMapping("/user/login")
    public ResponseEntity login(@RequestBody UserRequestDto requestDto){
        return ResponseEntity.ok()
                .body(userService.login(requestDto));
    }

    @Auth
    @PostMapping("/user/logout")
    public ResponseEntity logout(){
        return ResponseEntity.ok()
                .body(userService.logout());
    }

    @Auth
    @PostMapping("/test")
    public ResponseEntity test(){
        Map<String, String> map= new HashMap<>();
        map.put("Test", "login 테스트 완료");
        return ResponseEntity.ok().body(map);
    }

    @PostMapping("/user/refresh")
    public ResponseEntity refresh(){
        return ResponseEntity.ok().body(userService.refresh());
    }


    @PostMapping("/kiosk/login")
    public ResponseEntity<KioskUserResponseDto> kioskLogin(@RequestBody UserRequestDto requestDto){
        System.out.println("================"+requestDto.getClassNo());
        return ResponseEntity.ok()
                .body(userService.kioskLogin(requestDto));
    }


}
