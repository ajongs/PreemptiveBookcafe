package com.preemptivebookcafe.api.serviceImpl.user;

import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.user.UserService;
import javafx.util.converter.LocalDateTimeStringConverter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; //생성자 주입

    @Override
    public UserResponseDto signUp(UserRequestDto requestDto) {
        //여기서 유저 중복 체크 하고
        Optional<User> optionalUser = userRepository.findByClassNo(requestDto.getClassNo());
        if(optionalUser.isPresent()){
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }

        //UUID v4 특성상 겹칠 확률이 있긴함 -> 현재시간을 이용하여 더욱 겹치지 않도록 함.
        String salt = UUID.randomUUID().toString() + LocalDateTime.now();


        User user = new User(requestDto.getClassNo(), requestDto.getPassword(), requestDto.getEmail(), salt);
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setClassNo(requestDto.getClassNo());
        userResponseDto.setEmail(requestDto.getEmail());



        return userResponseDto;
    }
}
