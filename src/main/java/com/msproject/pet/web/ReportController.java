package com.msproject.pet.web;

import com.msproject.pet.service.ReportService;
import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080")
@Slf4j
@RequiredArgsConstructor
@RestController("/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/insert")
    public void insertReport(@RequestBody  ReportDto reportDto){
        reportService.insertReport(reportDto);
    }
}
