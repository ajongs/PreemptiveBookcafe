package com.preemptivebookcafe.api.serviceImpl.seat;

import com.preemptivebookcafe.api.dto.seat.SeatRequestDto;
import com.preemptivebookcafe.api.dto.seat.SeatResponseDto;
import com.preemptivebookcafe.api.dto.user.KioskUserResponseDto;
import com.preemptivebookcafe.api.dto.user.UserRequestDto;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import com.preemptivebookcafe.api.enums.SeatStatus;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.seat.SeatRepository;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.async.AsyncService;
import com.preemptivebookcafe.api.service.log.LogService;
import com.preemptivebookcafe.api.service.seat.SeatService;
import com.preemptivebookcafe.api.service.user.UserService;
import com.preemptivebookcafe.api.serviceImpl.firebase.FirebaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final UserService userService;
    private final LogService logService;
    private final AsyncService asyncService;
    private final FirebaseServiceImpl firebaseService;

    @Value("${rg_sleepTime}")
    private long registerSleepTime;

    @Value("${rp_sleepTime}")
    private long reportSleepTime;

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
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT); // 변경 할지 생각하자
        }
        if(optionalSeatEntity.get().getStatus().equals(SeatStatus.USED)){
            throw new RequestInputException(ErrorEnum.SEAT_ALREADY_USED);
        }
        //사용자 확인
        Optional<User> optionalUserEntity = userRepository.findByClassNo(requestDto.getUser().getClassNo());
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
        }
        //사용자가 이미 좌석 선택시 거부
        User user = optionalUserEntity.get();
        Optional<Seat> optionalUserHasSeat = seatRepository.findByUser(user);
        if(optionalUserHasSeat.isPresent()){
            Seat testSeat = optionalUserHasSeat.get();
            System.out.println("testSeat.getId() = " + testSeat.getId());
            System.out.println("testSeat.getStatus() = " + testSeat.getStatus());
            User isSameUser = testSeat.getUser();
            System.out.println("isSameUser.getClassNo() = " + isSameUser.getClassNo());
            throw new RequestInputException(ErrorEnum.ALREADY_HAS_SEAT);
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

        //비동기 타이머 메소드 (퇴실처리)
        asyncService.exitAsyncTimer(seat, LogEventEnum.REGISTER);
        //등록 로그 등록
        logService.createRegisterLog(seat.getUser(), seat);

        return convertToDto(seat);
    }
//TODO 자리등록시 다른 자리에 앉는 중이면 좌석 변경 시키던가/ 오류 메세지 띄우던가
    @Override
    public SeatResponseDto reportSeat(SeatRequestDto requestDto) {

        //로그인 검증
        Long classNo = userService.getLoginClassNo();
        //자리비움 신고
        Optional<Seat> optionalSeatEntity = seatRepository.findById(requestDto.getId());
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);// 유효하지 않은 좌석 error 이넘 만들어야함
        }
        if(optionalSeatEntity.get().getStatus().equals(SeatStatus.AWAY) || optionalSeatEntity.get().getStatus().equals(SeatStatus.EMPTY)){
            throw new RequestInputException(ErrorEnum.DO_NOT_REPORT);
        }




        Seat seat = optionalSeatEntity.get(); //TODO 나중에 자세히 보자
        //신고 메세지 보내기
        String token = seat.getUser().getFireToken();
        firebaseService.sendReportMessage(token);
        long between = ChronoUnit.SECONDS.between(seat.getRegisterAt(), LocalDateTime.now());
        long remainTime =  registerSleepTime - between;
        System.out.println("------------remainTime------------"+remainTime);
        if(remainTime < reportSleepTime){
            throw new RequestInputException(ErrorEnum.SEAT_INSUFFICIENT_TIME);
        }
        seat.changeSeatStatus(SeatStatus.AWAY);
        seatRepository.save(seat);
        asyncService.exitAsyncTimer(seat, LogEventEnum.REPORT);
        //신고 로그 등록 ( 신고자, 신고테이블)
        logService.createReportLog(seat.getUser(), userService.getUser(), seat);

        return convertToDto(seat);
    }

    @Override
    public SeatResponseDto exitSeat(SeatRequestDto requestDto) {
        Long classNo = requestDto.getUser().getClassNo();

        Optional<User> optionalUserEntity = userRepository.findByClassNo(classNo);
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
        }
        User user = optionalUserEntity.get();
        //seat의 유저부분만 삭제해야지
        Optional<Seat> optionalSeatEntity = seatRepository.findByUser(user);
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_HAS_SEAT);
        }

        Seat seat = optionalSeatEntity.get();
        if(seat.getId() != requestDto.getId()){
            throw new RequestInputException(ErrorEnum.INCONSISTENCY_SEAT_ID);
        }
        if(seat.getUser().equals(user) && requestDto.getId().equals(seat.getId())){
            seat.exit();
        }
        seatRepository.save(seat);
        return convertToDto(seat);
    }

    private void validUserCheck(Long userId){
        Optional<Seat> optionalSeatEntity = seatRepository.findById(userId);
        if(!optionalSeatEntity.isPresent()){ //null일때 오류
            throw new RequestInputException(ErrorEnum.SEAT_ALREADY_USED); // 변경 할지 생각하자
        }
    }

    private SeatResponseDto convertToDto(Seat seat){
        SeatResponseDto seatResponseDto = new SeatResponseDto();
        seatResponseDto.setUpdatedAt(seat.getUpdatedAt());
        seatResponseDto.setRegisterAt(seat.getRegisterAt());
        seatResponseDto.setStatus(seat.getStatus());
        seatResponseDto.setLeftOn(seat.getLeftOn());
        seatResponseDto.setId(seat.getId());
        seatResponseDto.setUser(seat.getUser());

        return seatResponseDto;
    }

    @Override
    public SeatResponseDto changeSeat(SeatRequestDto requestDto) {
        //TODO requestDTO 에서 변경하려는 seat id 를 받아오고
        long seat_id = requestDto.getId();
        long classNo = requestDto.getUser().getClassNo();

        //TODO classNo가 존재하는지 체크 & 좌석이 있는지 체크
        Optional<User> optionalUserEntity = userRepository.findByClassNo(classNo);
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
        }
        User user = optionalUserEntity.get();

        //기존 좌석 체크
        Optional<Seat> optionalUserHasSeatEntity = seatRepository.findByUser(user);
        if(!optionalUserHasSeatEntity.isPresent()){ //유저가 사용중인 좌석이 없다면 오류
            throw new RequestInputException(ErrorEnum.NOT_HAS_SEAT);
        }
        Seat userHasSeat = optionalUserHasSeatEntity.get();



        //TODO repository 가서 바꿀 seat id가 존재하는지 확인
        Optional<Seat> optionalSeatEntity = seatRepository.findById(seat_id);
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);
        }

        //TODO 해당 좌석에 이미 사용중인 사람이 있는경우
        Seat seat = optionalSeatEntity.get();
        if(seat.getUser()!=null){
            throw new RequestInputException(ErrorEnum.SEAT_ALREADY_USED);
        }
        //TODO 같다면 지금시간 - 등록시간 빼서 남은 시간으로 비동기 스레드 시작, 이전 스레드 찾아서 제거
        LocalDateTime exRegisterAt = userHasSeat.getRegisterAt();
        long between = ChronoUnit.SECONDS.between(exRegisterAt, LocalDateTime.now());
        long time = registerSleepTime- between;


        String exThreadName = userHasSeat.getUsedThread();
        Thread[] tArray = new Thread[Thread.activeCount()];
        Thread.enumerate(tArray);
        for (Thread thread : tArray) {
            if(thread.getName().equals(exThreadName)){
                System.out.println(thread.getName()+" 삭제");
                thread.interrupt();
            }
        }
        //이전 자리 퇴실처리
        userHasSeat.exit();
        seatRepository.save(userHasSeat);

        //새로운 자리 등록
        seat.changeSeat(user, exRegisterAt);
        seatRepository.save(seat);

        //출력을 위한 변환
        SeatResponseDto seatResponseDto = convertToDto(seat);

        //남은 시간만큼만 스레드 실행
        asyncService.changeThread(seat, time);
        return seatResponseDto;
    }

}
