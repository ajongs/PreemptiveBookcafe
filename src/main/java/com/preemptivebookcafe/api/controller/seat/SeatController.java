package com.preemptivebookcafe.api.controller.seat;

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
    public ResponseEntity selectSeat(@RequestBody SeatRequestDto requestDto){
        seatService.selectSeat(requestDto);
        return ResponseEntity.ok().body(requestDto.getId()+"번 좌석을 선택하였습니다.");
    }

}
