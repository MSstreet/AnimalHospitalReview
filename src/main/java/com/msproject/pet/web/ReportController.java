package com.msproject.pet.web;

import com.msproject.pet.service.ReportService;
import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/{reviewId}")
    public void insertReport(@PathVariable ReportDto reportDto){
        reportService.insertReport(reportDto);

    }
}
