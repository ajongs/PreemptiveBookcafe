package com.preemptivebookcafe.api.service.seat;

import com.preemptivebookcafe.api.dto.seat.SeatRequestDto;
import com.preemptivebookcafe.api.dto.seat.SeatResponseDto;

import java.util.List;

public interface SeatService {
    //모든 좌석 가져오기
    List<SeatResponseDto> getAllSeats();
    //좌석 선택 후 등록하기
    SeatResponseDto selectSeat(SeatRequestDto requestDto);
    //좌석 신고하기
    SeatResponseDto reportSeat(SeatRequestDto requestDto);

    SeatResponseDto exitSeat(SeatRequestDto requestDto);
}
