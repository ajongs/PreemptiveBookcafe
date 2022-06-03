package com.preemptivebookcafe.api.repository.log;

import com.preemptivebookcafe.api.dto.log.LogResponseDto;
import com.preemptivebookcafe.api.entity.Log;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.LogEventEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogRepository extends JpaRepository<Log, Long> {

    public Optional<List<Log>> findByLogEventAndReporter(LogEventEnum logEvent, User reporter);
    public Optional<List<Log>> findByLogEventAndUser(LogEventEnum logEvent, User user);
}
