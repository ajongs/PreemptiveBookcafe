package com.preemptivebookcafe.api.serviceImpl.log;

import com.preemptivebookcafe.api.dto.log.LogResponseDto;
import com.preemptivebookcafe.api.entity.Log;
import com.preemptivebookcafe.api.entity.Seat;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.log.LogRepository;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import com.preemptivebookcafe.api.service.log.LogService;
import com.preemptivebookcafe.api.util.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final UserRepository userRepository;
    private final Jwt jwt;

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

    @Override
    public List<LogResponseDto> getRegisterLog() {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");

        Map payload = jwt.getPayload(token, true);
        long id = Long.parseLong(String.valueOf(payload.get("id")));
        Optional<User> optionalUserEntity = userRepository.findById(id);
        if(!optionalUserEntity.isPresent()){
            throw new RequestInputException(ErrorEnum.INVALID_CLASS_NO);
            //변경해야함 유효하지 않은 토큰이라고 하는게 낫지?
        }
        User user = optionalUserEntity.get();
        System.out.println(user.getClassNo());


        Optional<List<Log>> optionalLogResponseDtoList = logRepository.findByLogEventAndUser(LogEventEnum.REGISTER, user);
        if(!optionalLogResponseDtoList.isPresent()){
            throw new RequestInputException(ErrorEnum.ID_ALREADY_EXISTS);
        }
        List<Log> list = optionalLogResponseDtoList.get();
        for (Log log : list) {
            System.out.println("log.getId() = " + log.getId());
            System.out.println("log.getLogEvent() = " + log.getLogEvent());
            System.out.println("log.getLogTime() = " + log.getLogTime());
        }
        return convertToLogDto(list);
    }

    private List<LogResponseDto> convertToLogDto(List<Log> logs){
        List<LogResponseDto> logResponseDtos = new ArrayList<>();
        for (Log log : logs) {
            LogResponseDto logResponseDto = new LogResponseDto();
            logResponseDto.setId(log.getId());
            logResponseDto.setLogEvent(log.getLogEvent());
            logResponseDto.setSeat(log.getSeat());
            logResponseDto.setUser(log.getUser());
            logResponseDto.setReporter(log.getReporter());
            logResponseDto.setLogTime(log.getLogTime());
            logResponseDtos.add(logResponseDto);
        }
        return logResponseDtos;
    }
}
