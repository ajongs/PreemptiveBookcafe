package com.preemptivebookcafe.api.entity;

import com.preemptivebookcafe.api.enums.SeatStatus;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Seat {

    @Id
    @Column(name = "seat_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    private SeatStatus status;

    //자리비움 시간
    private LocalDateTime leftOn;

    //자리등록 시간
    private LocalDateTime registerAt;
}
