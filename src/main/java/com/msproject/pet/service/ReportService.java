package com.msproject.pet.service;

import com.msproject.pet.repository.ReportRepository;
import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public void insertReport(ReportDto reportDto) {


        //reportRepository.save()
    }
}
