package com.preemptivebookcafe.api.serviceImpl.user;

import com.preemptivebookcafe.api.dto.user.KioskUserResponseDto;
import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.dto.user.UserResponseDto;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.SeatStatus;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.exception.TokenException;
import com.preemptivebookcafe.api.repository.seat.SeatRepository;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.seat.SeatService;
import com.preemptivebookcafe.api.service.user.UserService;
import com.preemptivebookcafe.api.util.Jwt;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; //생성자 주입
    private final SeatRepository seatRepository;
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
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
        }

        User user = optionalUser.get();
        if(!BCrypt.checkpw(requestDto.getPassword(), user.getPassword())){
            //비번 오류
            throw new RequestInputException(ErrorEnum.INVALID_PASSWORD);
        }

        Map<String, String> newToken = new HashMap<>();
        newToken.put(accessToken, jwt.createToken(user.getClassNo(), accessToken));
        newToken.put(refreshToken,jwt.createToken(user.getClassNo(), refreshToken));

        return newToken;
    }


    @Override
    public KioskUserResponseDto kioskLogin(UserRequestDto requestDto) {
        //여기서 사용자의 상태를 파악하고 isReportCancel false or true 넣어주어야함
        Optional<User> optionalUserEntity = userRepository.findByClassNo(requestDto.getClassNo());
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
        }
        User user = optionalUserEntity.get();
        Optional<Seat> optionalSeatEntity = seatRepository.findByUser(user);
        KioskUserResponseDto kioskUserResponseDto = new KioskUserResponseDto();
        if(!optionalSeatEntity.isPresent()){ //처음 방문하는 경우는 seat정보가 없음
            //kioskUserResponseDto.setNull(true);
            return kioskUserResponseDto; //TODO null로 반환하는거 프론트에서도 받을 수 있나?
        }


        Seat seat = optionalSeatEntity.get();
        //신고당한 좌석의 학번일 경우는 신고 푸는 로직 실행을 위해 isReportCancel 에 true 넣어준다 .
        if(seat.getStatus().equals(SeatStatus.AWAY)){
            //신고 스레드 없애야함
            String reportThread = seat.getReportThread();
            Thread[] tArray = new Thread[Thread.activeCount()];
            Thread.enumerate(tArray);
            for (Thread thread : tArray) {
                if(thread.getName().equals(reportThread)){
                    thread.interrupt();
                }
            }
            seat.updateSeatStatus(SeatStatus.USED);
            seat.updateReportThread(null);
            kioskUserResponseDto.setReportCancel("true");
            seatRepository.save(seat);
        } // 좌석 변경하려고 하는경우는 아래 로직을 따라감
        else kioskUserResponseDto.setReportCancel("false");

        kioskUserResponseDto.setId(seat.getId());
        kioskUserResponseDto.setUser(seat.getUser());
        kioskUserResponseDto.setStatus(seat.getStatus());
        kioskUserResponseDto.setLeftOn(seat.getLeftOn());
        kioskUserResponseDto.setRegisterAt(seat.getRegisterAt());
        kioskUserResponseDto.setUpdatedAt(seat.getUpdatedAt());

        return kioskUserResponseDto;
    }

    public Long getLoginClassNo(){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");

        Map payload = jwt.getPayload(token, true);
        return Long.parseLong(String.valueOf(payload.get("classNo")));
    }

    public User getUser(){
        Long classNo = getLoginClassNo();
        Optional<User> optionalUserEntity = userRepository.findByClassNo(classNo);
        if(!optionalUserEntity.isPresent()){
            throw new TokenException(ErrorEnum.NO_USER_IN_TOKEN);
        }
        return optionalUserEntity.get();
    }
}
