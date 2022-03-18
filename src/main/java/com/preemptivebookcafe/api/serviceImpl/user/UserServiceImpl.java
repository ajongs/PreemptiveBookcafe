package com.preemptivebookcafe.api.serviceImpl.user;

import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.user.UserService;
import com.preemptivebookcafe.api.util.Jwt;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; //생성자 주입
    private final Jwt jwt;

    @Value("${access_token}")
    private String accessToken;

    @Value("${refresh_token}")
    private String refreshToken;

    @Override
    public UserResponseDto signUp(UserRequestDto requestDto) {
        //여기서 유저 중복 체크 하고
        Optional<User> optionalUser = userRepository.findByClassNo(requestDto.getClassNo());
        if(optionalUser.isPresent()){
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }

        //UUID v4 특성상 겹칠 확률이 있긴함 -> 현재시간을 이용하여 더욱 겹치지 않도록 함.
        String salt = UUID.randomUUID().toString() + LocalDateTime.now();

        requestDto.setPassword(BCrypt.hashpw(requestDto.getPassword(), BCrypt.gensalt()));

        User user = new User(requestDto.getClassNo(), requestDto.getPassword(), requestDto.getEmail(), salt);
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setClassNo(requestDto.getClassNo());
        userResponseDto.setEmail(requestDto.getEmail());



        return userResponseDto;
    }

    @Override
    public Map login(UserRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findByClassNo(requestDto.getClassNo());
        if(!optionalUser.isPresent()){
            //존재하지 않는 classNO
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }

        User user = optionalUser.get();
        Map<String, String> newToken = new HashMap<>();
        newToken.put(accessToken, jwt.createToken(user.getClassNo(), accessToken));
        newToken.put(refreshToken,jwt.createToken(user.getClassNo(), refreshToken));

        return newToken;
    }
}
