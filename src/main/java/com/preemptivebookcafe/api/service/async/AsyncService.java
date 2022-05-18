package com.preemptivebookcafe.api.service.async;

import com.preemptivebookcafe.api.entity.Seat;
import org.springframework.scheduling.annotation.Async;

public interface AsyncService {

    @Async
    public void exitAsyncTimer(Seat seat);
}
