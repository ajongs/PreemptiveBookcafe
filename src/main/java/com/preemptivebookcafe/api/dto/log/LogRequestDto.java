package com.preemptivebookcafe.api.dto.log;

import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Data
public class LogRequestDto {
    private Long id;

    private LogEventEnum logEvent;

    private Seat seat;

    private User user;

    private User reporter;

    private LocalDateTime logTime;

}
