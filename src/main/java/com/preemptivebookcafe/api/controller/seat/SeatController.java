package com.preemptivebookcafe.api.controller.seat;

import com.preemptivebookcafe.api.annotation.Auth;
import com.preemptivebookcafe.api.dto.seat.SeatRequestDto;
import com.preemptivebookcafe.api.dto.seat.SeatResponseDto;
import com.preemptivebookcafe.api.service.seat.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    @GetMapping()
    public ResponseEntity getSeats(){
        return ResponseEntity.ok().body(seatService.getAllSeats());
    }

    //좌석 선택
    @PostMapping()
    public ResponseEntity<SeatResponseDto> selectSeat(@RequestBody SeatRequestDto requestDto){
        return ResponseEntity.ok().body(seatService.selectSeat(requestDto));
    }

    //부재중 좌석 신고
    @Auth
    @PostMapping("/report")
    public ResponseEntity<SeatResponseDto> reportSeat(@RequestBody SeatRequestDto requestDto){
        return ResponseEntity.ok().body(seatService.reportSeat(requestDto));
    }

    //퇴실
    @DeleteMapping()
    public ResponseEntity<SeatResponseDto> exitSeat(@RequestBody SeatRequestDto requestDto){
        return ResponseEntity.ok().body(seatService.exitSeat(requestDto));
    }
}
