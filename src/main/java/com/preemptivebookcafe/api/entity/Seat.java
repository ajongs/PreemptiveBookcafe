package com.preemptivebookcafe.api.entity;

import com.preemptivebookcafe.api.enums.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    //빌더 패턴을 이용하면 모든 매개변수를 받는 생성자가 필요로함.


    @Id
    @Column(name = "seat_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)//, cascade = {CascadeType.ALL}
    @JoinColumn(name = "user_id")
    private User user;

    private SeatStatus status;

    //자리비움 시간
    private LocalDateTime leftOn;

    //자리등록 시간
    private LocalDateTime registerAt;
    private LocalDateTime updatedAt;

}
