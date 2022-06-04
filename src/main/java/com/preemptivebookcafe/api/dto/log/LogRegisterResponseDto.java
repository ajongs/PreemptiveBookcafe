package com.preemptivebookcafe.api.dto.log;

import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogRegisterResponseDto {
    private Long id;

    private LogEventEnum logEvent;

    private Long seatId;

    private Long classNo;

    private LocalDateTime logTime;
}
