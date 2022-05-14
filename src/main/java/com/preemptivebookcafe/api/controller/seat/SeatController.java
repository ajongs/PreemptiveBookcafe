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
        return ResponseEntity.ok().body("logics");
    }

    @PostMapping()
    public ResponseEntity<SeatResponseDto> selectSeat(@RequestBody SeatRequestDto requestDto){
        return ResponseEntity.ok().body(seatService.selectSeat(requestDto));
    }

    @Auth
    @PostMapping("/report")
    public ResponseEntity<SeatResponseDto> reportSeat(@RequestBody SeatRequestDto requestDto){
        return ResponseEntity.ok().body(seatService.reportSeat(requestDto));
    }
}
