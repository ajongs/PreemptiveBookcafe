package com.preemptivebookcafe.api.serviceImpl.log;

import com.preemptivebookcafe.api.dto.log.LogResponseDto;
import com.preemptivebookcafe.api.entity.Log;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import com.preemptivebookcafe.api.repository.log.LogRepository;
import com.preemptivebookcafe.api.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public LogResponseDto createReportLog(User user, User reporter, Seat seat) {
        Log reportLog = new Log(user, reporter, seat);
        reportLog.changeLogEvent(LogEventEnum.REPORT);

        logRepository.save(reportLog);

        LogResponseDto logResponseDto = new LogResponseDto();
        logResponseDto.setId(reportLog.getId());
        logResponseDto.setLogEvent(reportLog.getLogEvent());
        logResponseDto.setSeat(reportLog.getSeat());
        logResponseDto.setUser(reportLog.getUser());
        logResponseDto.setLogTime(reportLog.getLogTime());
        return logResponseDto;
    }

    @Override
    public LogResponseDto createRegisterLog(User user, Seat seat) {
        Log registerLog = new Log(user, seat);
        registerLog.changeLogEvent(LogEventEnum.REGISTER);

        logRepository.save(registerLog);

        LogResponseDto logResponseDto = new LogResponseDto();
        logResponseDto.setId(registerLog.getId());
        logResponseDto.setLogEvent(registerLog.getLogEvent());
        logResponseDto.setSeat(registerLog.getSeat());
        logResponseDto.setUser(registerLog.getUser());
        logResponseDto.setLogTime(registerLog.getLogTime());
        return logResponseDto;
    }
}
