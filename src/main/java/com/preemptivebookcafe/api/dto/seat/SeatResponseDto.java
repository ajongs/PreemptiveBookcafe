package com.preemptivebookcafe.api.dto.seat;

import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.SeatStatus;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
public class SeatResponseDto {
    private Long id;
    private User user;
    private SeatStatus status;

    //자리비움 시간
    private LocalDateTime leftOn;

    //자리등록 시간
    private LocalDateTime registerAt;
    private LocalDateTime updatedAt;
}
