package com.preemptivebookcafe.api.dto.log;

import com.preemptivebookcafe.api.enums.LogEventEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogReportResponseDto {
    private Long id;

    private LogEventEnum logEvent;

    private Long seatId;

    private Long classNo;

    private LocalDateTime logTime;
}
