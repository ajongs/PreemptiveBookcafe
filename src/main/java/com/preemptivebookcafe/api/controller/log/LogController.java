package com.preemptivebookcafe.api.controller.log;

import com.preemptivebookcafe.api.dto.log.LogResponseDto;
import com.preemptivebookcafe.api.entity.Log;
import com.preemptivebookcafe.api.service.log.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @GetMapping("/log/register")
    public ResponseEntity<List<LogResponseDto>> getLogRegister(){
        return ResponseEntity.ok().body(logService.getRegisterLog());
    }

    @GetMapping("log/report")
    public ResponseEntity<List<LogResponseDto>> getLogReport() {
        return ResponseEntity.ok().body(logService.getReportLog());
    }

}
