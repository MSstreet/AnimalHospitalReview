package com.msproject.pet.service;

import com.msproject.pet.entity.*;
import com.msproject.pet.model.Header;
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

    public long getHelpfulCount(Long id) {
        return helpfulRepositoryCustom.getHelpfulCount(id);
    }

    public Boolean checkHelpful(Long uid, Long rid) {
        if(helpfulRepositoryCustom.helpfulChk(uid,rid)){
            return true;
        }else {
            return false;
        }
    }

    public Header<HelpfulDto> insertHelpful(HelpfulDto helpfulDto) {
        if(checkHelpful(helpfulDto.getUserNum(),helpfulDto.getReviewNum())){
            Optional<ReviewEntity> reviewEntity = reviewRepository.findById(helpfulDto.getReviewNum());
            ReviewEntity review = reviewEntity.orElseThrow();

            Optional<UserEntity> userEntity = userRepository.findById(helpfulDto.getUserNum());
            UserEntity user = userEntity.orElseThrow();

            HelpfulEntity saveHelpful = helpfulRepository.save(HelpfulEntity.of(helpfulDto, review, user));
            HelpfulDto helpfulDtoResult = HelpfulDto.fromEntity(saveHelpful);

            return Header.OK(helpfulDtoResult);
        }else{
            HelpfulEntity helpfulEntity = helpfulRepositoryCustom.findOneHelpful(helpfulDto.getUserNum(),helpfulDto.getReviewNum());
            helpfulEntity.changeHelpfulState(helpfulDto.getHelpFul());
            helpfulEntity.setUpdatedAt(LocalDateTime.now());
            HelpfulDto helpfulDtoResult = HelpfulDto.fromEntity(helpfulRepository.save(helpfulEntity));

            return Header.OK(helpfulDtoResult);
        }
    }
}
