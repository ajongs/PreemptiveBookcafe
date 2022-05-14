package com.preemptivebookcafe.api.serviceImpl.seat;

import com.preemptivebookcafe.api.dto.seat.SeatRequestDto;
import com.preemptivebookcafe.api.dto.seat.SeatResponseDto;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.SeatStatus;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.seat.SeatRepository;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final UserRepository userRepository;
    private final SeatRepository seatRepository;

    //모든 좌석 얻어오기
    @Override
    public List<SeatResponseDto> getAllSeats() {
        List<SeatResponseDto> list = new ArrayList<>();
        List<Seat> all = seatRepository.findAll();
        for(Seat s : all) {
            SeatResponseDto dto = new SeatResponseDto();
            dto.setId(s.getId());
            dto.setUser(s.getUser());
            dto.setStatus(s.getStatus());
            dto.setRegisterAt(s.getRegisterAt());
            dto.setLeftOn(s.getLeftOn());
            dto.setUpdatedAt(s.getUpdatedAt());
            list.add(dto);
        }
        return list;
    }

    //좌석 선택 후 등록하기
    @Override
    public SeatResponseDto selectSeat(SeatRequestDto requestDto) {
        //해당 좌석 상태 확인 후
        Optional<Seat> optionalSeatEntity = seatRepository.findById(requestDto.getId());
        if(!optionalSeatEntity.isPresent()){ //null일때 오류
            throw new RequestInputException(ErrorEnum.SEAT_ALREADY_USED); // 변경 할지 생각하자
        }
        //사용자 확인
        Optional<User> optionalUserEntity = userRepository.findByClassNo(requestDto.getUser().getClassNo());
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NO_USER_IN_TOKEN);
        }



        //문제 없다면 등록 진행
        Seat seat = Seat.builder()
                .id(optionalSeatEntity.get().getId())
                .user(optionalUserEntity.get())
                .status(SeatStatus.USED)
                .registerAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        seatRepository.save(seat);

        SeatResponseDto seatResponseDto = new SeatResponseDto();
        seatResponseDto.setUpdatedAt(seat.getUpdatedAt());
        seatResponseDto.setStatus(seat.getStatus());
        seatResponseDto.setLeftOn(seat.getLeftOn());
        seatResponseDto.setId(seat.getId());
        seatResponseDto.setUser(seat.getUser());
        return seatResponseDto;
    }

    @Override
    public SeatResponseDto reportSeat(SeatRequestDto requestDto) {
        //로그인 검증
        validUserCheck(requestDto.getId());
        //자리비움 신고
        Optional<Seat> optionalSeatEntity = seatRepository.findById(requestDto.getId());
        if(!optionalSeatEntity.isPresent()){
            //throw new RequestInputException() 유효하지 않은 좌석 error 이넘 만들어야함
        }


        if(optionalSeatEntity.get().getStatus().equals(SeatStatus.AWAY) || optionalSeatEntity.get().getStatus().equals(SeatStatus.EMPTY)){
            throw new RequestInputException(ErrorEnum.DO_NOT_REPORT);
        }

        Seat seat = optionalSeatEntity.get(); //TODO 나중에 자세히 보자
        seat.changeSeatStatus(SeatStatus.AWAY);
        seatRepository.save(seat);
        //신고 로그 등록 ( 신고자, 신고테이블)

        SeatResponseDto seatResponseDto = new SeatResponseDto();
        seatResponseDto.setUpdatedAt(seat.getUpdatedAt());
        seatResponseDto.setStatus(seat.getStatus());
        seatResponseDto.setLeftOn(seat.getLeftOn());
        seatResponseDto.setId(seat.getId());
        seatResponseDto.setUser(seat.getUser());
        return seatResponseDto;
    }

    private void validUserCheck(Long userId){
        Optional<Seat> optionalSeatEntity = seatRepository.findById(userId);
        if(!optionalSeatEntity.isPresent()){ //null일때 오류
            throw new RequestInputException(ErrorEnum.SEAT_ALREADY_USED); // 변경 할지 생각하자
        }
    }
}
