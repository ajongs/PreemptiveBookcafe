package com.preemptivebookcafe.api.dto.seat;

import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.SeatStatus;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
public class SeatRequestDto {
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;
}
