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
    private final HelpfulRepositoryCustom helpfulRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public HelpfulDto createHelpful(HelpfulDto helpfulDto) {
        boolean chk = checkWish(helpfulDto.getReviewNum(),helpfulDto.getUserNum());

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(helpfulDto.getReviewNum());
        ReviewEntity review = reviewEntity.orElseThrow();

        Optional<UserEntity> userEntity = userRepository.findById(helpfulDto.getUserNum());
        UserEntity user = userEntity.orElseThrow();

        if(chk) {
            helpfulRepository.save(HelpfulEntity.of(helpfulDto, review, user));
        }else{
            updateHelpful(helpfulDto);
        }

        return helpfulDto;
    }

    public Boolean checkWish(Long uid, Long rid) {

        if(helpfulRepositoryCustom.findOneHelpful(uid, rid) == null){
            return true;
        }
        return false;
    }

    public HelpfulDto updateHelpful(HelpfulDto helpfulDto) {

        HelpfulEntity entity = helpfulRepositoryCustom.findOneHelpful(helpfulDto.getUserNum(),helpfulDto.getReviewNum());
        int postHelpfulState = helpfulDto.getHelpFul();
        int preHelpfulState = entity.getHelpFul();

        if(postHelpfulState == 1){
            // 추가
        }else if (postHelpfulState == 2){
            // 추가
        }

        entity.changeHelpfulState(helpfulDto.getHelpFul());
        entity.setUpdatedAt(LocalDateTime.now());
        return HelpfulDto.fromEntity(helpfulRepository.save(entity));
    }
}
