package com.preemptivebookcafe.api.serviceImpl.user;

import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.user.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; //생성자 주입

    @Override
    public UserResponseDto signUp(User user) {
        //여기서 유저 중복 체크 하고
        Optional<User> optionalUser = userRepository.findByClassNo(user.getClassNo());
        if(optionalUser.isPresent()){
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }
        userRepository.save(user);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setClassNo(user.getClassNo());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }
}
