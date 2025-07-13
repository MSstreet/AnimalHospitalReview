package com.msproject.pet.service;

import com.msproject.pet.entity.*;
import com.msproject.pet.model.Header;
import com.msproject.pet.repository.HelpfulRepository;
import com.msproject.pet.repository.HelpfulRepositoryCustom;
import com.msproject.pet.repository.ReviewRepository;

import com.msproject.pet.web.dtos.HelpfulDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HelpfulService {

    private final HelpfulRepository helpfulRepository;
    private final HelpfulRepositoryCustom helpfulRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public long getHelpfulCount(Long id) {
        return helpfulRepositoryCustom.getHelpfulCount(id);
    }

    public Header<HelpfulDto> getHelpful(Long uid, Long rid) {
        HelpfulDto helpfulDto = HelpfulDto.fromEntity(helpfulRepositoryCustom.findOneHelpful(uid, rid));
        if(helpfulDto != null){
            return Header.OK(helpfulDto);
        }else {
            return null;
        }
    }

    public Header<HelpfulDto> insertHelpful(HelpfulDto helpfulDto) {
        log.info("Starting insertHelpful with data: {}", helpfulDto);
        
        // null 체크 추가
        if (helpfulDto.getUserNum() == null || helpfulDto.getReviewNum() == null) {
            log.error("Required data is missing: userNum={}, reviewNum={}", helpfulDto.getUserNum(), helpfulDto.getReviewNum());
            return Header.ERROR("필수 데이터가 누락되었습니다.");
        }
        
        // 기존 helpful 데이터가 있는지 확인
        HelpfulEntity existingHelpful = helpfulRepositoryCustom.findOneHelpful(helpfulDto.getUserNum(), helpfulDto.getReviewNum());
        log.info("Existing helpful found: {}", existingHelpful);
        
        if (existingHelpful == null) {
            // 새로운 helpful 데이터 생성
            log.info("Creating new helpful data");
            Optional<ReviewEntity> reviewEntity = reviewRepository.findById(helpfulDto.getReviewNum());
            ReviewEntity review = reviewEntity.orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

            Optional<UserEntity> userEntity = userRepository.findById(helpfulDto.getUserNum());
            UserEntity user = userEntity.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            HelpfulEntity saveHelpful = helpfulRepository.save(HelpfulEntity.of(helpfulDto, review, user));
            HelpfulDto helpfulDtoResult = HelpfulDto.fromEntity(saveHelpful);
            log.info("New helpful created: {}", helpfulDtoResult);

            return Header.OK(helpfulDtoResult);
        } else {
            // 기존 데이터 업데이트
            log.info("Updating existing helpful data");
            existingHelpful.changeHelpfulState(helpfulDto.getHelpFul());
            existingHelpful.setUpdatedAt(LocalDateTime.now());
            HelpfulDto helpfulDtoResult = HelpfulDto.fromEntity(helpfulRepository.save(existingHelpful));
            log.info("Helpful updated: {}", helpfulDtoResult);

            return Header.OK(helpfulDtoResult);
        }
    }
}
