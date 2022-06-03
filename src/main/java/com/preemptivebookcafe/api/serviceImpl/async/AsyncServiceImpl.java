package com.preemptivebookcafe.api.serviceImpl.async;

import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import com.preemptivebookcafe.api.enums.SeatStatus;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.seat.SeatRepository;
import com.preemptivebookcafe.api.service.async.AsyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {

    private final SeatRepository seatRepository;
    @Value("${rg_sleepTime}")
    private long registerSleepTime;

    @Value("${rp_sleepTime}")
    private long reportSleepTime;

    @Async
    @Override
    public void exitAsyncTimer(Seat seat, LogEventEnum eventEnum) {

        //System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
        Optional<Seat> optionalSeatEntity = seatRepository.findById(seat.getId());
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.NOT_EXIST_SEAT);
        }
        Seat seatEntity = optionalSeatEntity.get();

        if(eventEnum.equals(LogEventEnum.REGISTER)){
            //세시간 스레드는 항상 시작할때 저장하고 sleep 함
            try {
                seatEntity.updateThread(Thread.currentThread().getName());
                seatRepository.save(seatEntity);
                Thread.sleep(registerSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("________________인터럽트 걸림--------------");
            }
        }
        else if(eventEnum.equals(LogEventEnum.REPORT)){
            //45분 신고 스레드는 시간 계산해서 45분보다 적으면 신고 못하게 하고 슬립
            seatEntity.updateReportThread(Thread.currentThread().getName());
            seatRepository.save(seatEntity);
            try {
                Thread.sleep(reportSleepTime);

                String name = seatEntity.getUsedThread();
                Thread[] tArray = new Thread[Thread.activeCount()];
                Thread.enumerate(tArray);
                for (Thread thread : tArray) {
                    if(thread.getName().equals(name)){
                        thread.interrupt();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        seatEntity.exit();
        seatRepository.save(seatEntity);

    }

    @Async
    public void changeThread(Seat seat, long time){
        String threadName = Thread.currentThread().getName();
        System.out.println("threadName = " + threadName);
        seat.updateThread(threadName);
        seatRepository.save(seat);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        seat.exit();
        seatRepository.save(seat);
    }


    //@Async 규칙 1. 같은 클래스안에서 호출하면 안됨.(내부 호출)
    // 규칙 2. 반환 타입이 void 이어야함.
    // 규칙 3. public 만 가능


    @Override
    public void reportCancel(Long classNo) {
        /*
        Optional<Seat> optionalSeatEntity = seatRepository.findByClassNo(classNo);
        if(!optionalSeatEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }*/
        //좌석 번호 찾아서 있으면
    }
}
