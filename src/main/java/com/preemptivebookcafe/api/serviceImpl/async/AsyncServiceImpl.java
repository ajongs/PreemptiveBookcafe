package com.preemptivebookcafe.api.serviceImpl.async;

import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.enums.ErrorEnum;
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
    public void exitAsyncTimer(Seat seat) {
        try {
            Thread.sleep(10000); //세시간 10800000
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Seat 퇴실 처리
        Optional<Seat> optionalSeatEntity = seatRepository.findById(seat.getId());
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);
        }

        Seat modifiedSeat = optionalSeatEntity.get();
        modifiedSeat.exit();

        seatRepository.save(modifiedSeat);
    }
}
