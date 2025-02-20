package com.msproject.pet.service;
import com.msproject.pet.entity.*;
import com.msproject.pet.model.Header;
import com.msproject.pet.model.Pagination;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.repository.ReportRepository;

import com.msproject.pet.repository.ReportRepositoryCustom;
import com.msproject.pet.repository.ReviewRepository;
import com.msproject.pet.web.dtos.ReportDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportRepositoryCustom reportRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReportDto insertReport(ReportDto reportDto) {

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reportDto.getReviewNum());
        ReviewEntity review = reviewEntity.orElseThrow();

        Optional<UserEntity> userEntity = userRepository.findById(reportDto.getUserNum());
        UserEntity user = userEntity.orElseThrow();

        ReportEntity reportEntity = reportRepository.save(ReportEntity.of(reportDto,review,user));
        return ReportDto.fromEntity(reportEntity);
    }

    public Header<List<ReportDto>> getReportList(Pageable pageable, SearchCondition searchCondition) {
        List<ReportDto> dtos = new ArrayList<>();
        Page<ReportEntity> reportEntities = reportRepositoryCustom.findAllBySearchCondition(pageable,searchCondition);

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
        ReportEntity entity = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("신고 글을 찾을 수 없습니다."));
        return ReportDto.fromEntity(entity);
    }

    public void delete(Long id) {
        ReportEntity entity = reportRepository.findById(id).orElseThrow(() -> new RuntimeException("신고 글을 찾을 수 없습니다."));
        reportRepository.delete(entity);
    }
}
