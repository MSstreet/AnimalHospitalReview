package com.msproject.pet.service;

import com.msproject.pet.entity.*;
import com.msproject.pet.repository.HelpfulRepository;
import com.msproject.pet.repository.HelpfulRepositoryCustom;
import com.msproject.pet.repository.ReviewRepository;
import com.msproject.pet.web.dtos.HelpfulDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class HelpfulService {

    private final HelpfulRepository helpfulRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public HelpfulDto createHelpful(HelpfulDto helpfulDto) {

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(helpfulDto.getReviewNum());
        ReviewEntity review = reviewEntity.orElseThrow();

        Optional<UserEntity> userEntity = userRepository.findById(helpfulDto.getUserNum());
        UserEntity user = userEntity.orElseThrow();

        helpfulRepository.save(HelpfulEntity.of(helpfulDto, review, user));

        return helpfulDto;
    }

}
