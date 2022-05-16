package com.preemptivebookcafe.api.repository.log;

import com.preemptivebookcafe.api.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
