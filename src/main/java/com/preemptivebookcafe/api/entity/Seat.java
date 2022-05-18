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
@AllArgsConstructor //빌더 패턴을 이용하면 모든 매개변수를 받는 생성자가 필요로함.
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)//, cascade = {CascadeType.ALL}  // 기본이 원래 FetchType.EAGER
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    //자리비움 시간
    private LocalDateTime leftOn;

    //자리등록 시간
    private LocalDateTime registerAt;
    private LocalDateTime updatedAt;

    public void changeSeatStatus(SeatStatus status){
        this.status = status;
        this.leftOn = LocalDateTime.now();
    }

    //퇴실 메소드
    public void exit() {
        this.user = null;
        this.registerAt = null;
        this.status = SeatStatus.EMPTY;
        this.updatedAt = LocalDateTime.now();
        this.leftOn = null;
    }
}
