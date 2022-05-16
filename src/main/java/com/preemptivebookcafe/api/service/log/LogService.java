package com.preemptivebookcafe.api.service.log;


import com.preemptivebookcafe.api.dto.log.LogResponseDto;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.LogEventEnum;

public interface LogService {
    LogResponseDto createReportLog(User user, User reporter, Seat seat);
    LogResponseDto createRegisterLog(User user, Seat seat);
}
