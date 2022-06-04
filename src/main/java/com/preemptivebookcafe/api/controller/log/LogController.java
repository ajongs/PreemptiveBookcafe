package com.preemptivebookcafe.api.controller.log;

import com.preemptivebookcafe.api.annotation.Auth;
import com.preemptivebookcafe.api.dto.log.LogRegisterResponseDto;
import com.preemptivebookcafe.api.dto.log.LogReportResponseDto;
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

    @Auth
    @GetMapping("/log/register")
    public ResponseEntity<List<LogRegisterResponseDto>> getLogRegister(){
        return ResponseEntity.ok().body(logService.getRegisterLog());
    }

    @Auth
    @GetMapping("log/report")
    public ResponseEntity<List<LogReportResponseDto>> getLogReport() {
        return ResponseEntity.ok().body(logService.getReportLog());
    }

}
