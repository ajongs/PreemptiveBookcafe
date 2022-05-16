package com.preemptivebookcafe.api.entity;

import com.preemptivebookcafe.api.enums.LogEventEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Log {
    @Id
    @Column(name = "log_id")
    private Long id;

    @Column(name = "log_event")
    private LogEventEnum logEvent;

    @ManyToOne
    @JoinColumn(name="seat_id")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="user_id")
    private User reporter;

    @Column(name="log_time")
    private LocalDateTime logTime;
}
