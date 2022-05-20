package com.preemptivebookcafe.api.serviceImpl.async;

import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import com.preemptivebookcafe.api.enums.SeatStatus;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.seat.SeatRepository;
import com.preemptivebookcafe.api.service.async.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {

    private final SeatRepository seatRepository;

    @Async
    @Override
    public void exitAsyncTimer(Seat seat, LogEventEnum eventEnum) {
        /*
        Optional<Seat> optionalSeatEntity = seatRepository.findById(seat.getId());
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);
        }
        Seat modifiedSeat = optionalSeatEntity.get();*/
        if(eventEnum.equals(LogEventEnum.REGISTER)){
            try {
                Thread.sleep(40000); //세시간 10800000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else if(eventEnum.equals(LogEventEnum.REPORT)){
            try {
                Thread.sleep(3000); //세시간 10800000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Optional<Seat> optionalSeatEntity = seatRepository.findById(seat.getId());
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);
        }
        Seat modifiedSeat = optionalSeatEntity.get();
        if(!modifiedSeat.getUser().getId().equals(seat.getUser().getId())){
            System.out.println("리턴됩니다.");
            return;
        }
        //Seat 퇴실 처리
        modifiedSeat.exit();
        seatRepository.save(modifiedSeat);
    }

    //@Async 규칙 1. 같은 클래스안에서 호출하면 안됨.(내부 호출)
    // 규칙 2. 반환 타입이 void 이어야함.
    // 규칙 3. public 만 가능


}
