package com.msproject.pet.web;

import com.msproject.pet.model.Header;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.service.ReportService;
import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController("/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/insert")
    public void insertReport(@RequestBody ReportDto reportDto){
        reportService.insertReport(reportDto);
    }

    @GetMapping("/")
    public Header<List<ReportDto>> getReportList(@PageableDefault(sort = {"idx"}) Pageable pageable,
                                                 SearchCondition searchCondition) {
        return reportService.getReportList(pageable, searchCondition);
    }

//    @GetMapping("/{idx}")
//    public Header<ReportDto> getReport(@PathVariable Integer idx) {
//
//    }
}
