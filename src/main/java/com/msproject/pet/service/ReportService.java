package com.msproject.pet.service;
import com.msproject.pet.entity.ReportEntity;
import com.msproject.pet.model.Header;
import com.msproject.pet.model.Pagination;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.repository.ReportRepository;

import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportDto insertReport(ReportDto reportDto) {
        ReportEntity reportEntity = reportRepository.save(ReportEntity.of(reportDto));
        return ReportDto.fromEntity(reportEntity);
    }

    public Header<List<ReportDto>> getReportList(Pageable pageable, SearchCondition searchCondition) {
        List<ReportDto> dtos = new ArrayList<>();
        Page<ReportEntity> reportEntities = reportRepository.findAll(pageable);

        for(ReportEntity reportEntity : reportEntities) {
            ReportDto dto = ReportDto.fromEntity(reportEntity);
            dtos.add(dto);
        }

        Pagination pagination = new Pagination(
                (int)reportEntities.getTotalElements()
                , pageable.getPageNumber() + 1
                , pageable.getPageSize()
                , 10
        );

        return Header.OK(dtos, pagination);
    }

    public ReportDto getReport(Long id) {
        ReportEntity entity = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("신고글을 찾을 수 없습니다."));
        return ReportDto.fromEntity(entity);
    }

    public void delete(Long id) {
        ReportEntity entity = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("신고글을 찾을 수 없습니다."));
        reportRepository.delete(entity);
    }
}
