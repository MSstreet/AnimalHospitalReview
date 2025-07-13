package com.msproject.pet.service;

import com.msproject.pet.entity.PetHospitalEntity;
import com.msproject.pet.entity.ReviewEntity;
import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.entity.UserRepository;
import com.msproject.pet.model.Header;
import com.msproject.pet.model.Pagination;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.repository.PetHospitalRepository;
import com.msproject.pet.repository.ReviewRepository;

import com.msproject.pet.repository.ReviewRepositoryCustom;
import com.msproject.pet.web.dtos.ReviewDto;
import com.msproject.pet.web.dtos.ReviewListWithHelpfulCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class ReviewService {

    private String path = "C:\\upload\\";

    private final ReviewRepository reviewRepository;

    private final PetHospitalRepository petHospitalRepository;

    private  final UserRepository userRepository;

    private final ReviewRepositoryCustom reviewRepositoryCustom;

    public double GetReviewAvg(Long id) {
        double reviewAvg = reviewRepositoryCustom.getReviewAverageScore(id);
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.0");
        reviewAvg = Double.parseDouble(df.format(reviewAvg));
        return reviewAvg;
    }

    public ReviewEntity ReviewCreate(ReviewDto reviewDto) throws Exception{

        Optional<PetHospitalEntity> petHospitalEntity = petHospitalRepository.findById(reviewDto.getPetHospitalNum());
        PetHospitalEntity pet = petHospitalEntity.orElseThrow();

        Optional<UserEntity> userEntity = userRepository.findById(reviewDto.getUserNum());
        UserEntity user = userEntity.orElseThrow();

        float avgScore = (float)(reviewDto.getEffectScore() + reviewDto.getPriceScore() + reviewDto.getKindnessScore()) / 3;

        DecimalFormat df = new DecimalFormat("0.0");
        avgScore = Float.parseFloat(df.format(avgScore));

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .petHospitalEntity(pet)
                .hospitalName(pet.getHospitalName())
                .userEntity(user)
                .content(reviewDto.getContent())
                .kindnessScore(reviewDto.getKindnessScore())
                .effectScore(reviewDto.getEffectScore())
                .priceScore(reviewDto.getPriceScore())
                .deleteYn(reviewDto.isDeleteYn())
                .tmpScore(avgScore)
                .fileName(reviewDto.getFileName())
                .originalFileName(reviewDto.getOriginalFileName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return reviewRepository.save(reviewEntity);
    }

    public ReviewEntity update(ReviewDto reviewDto) {

        ReviewEntity entity = reviewRepository.findById(reviewDto.getReviewId()).orElseThrow(()-> new RuntimeException("존재하지 않는 리뷰입니다."));
        float avgScore = (float)(reviewDto.getEffectScore() + reviewDto.getPriceScore() + reviewDto.getKindnessScore()) / 3;
        System.out.println("평균값 확인" + avgScore);
        DecimalFormat df = new DecimalFormat("0.0");
        avgScore = Float.parseFloat(df.format(avgScore));

        entity.changeReview(reviewDto.getContent(), avgScore,reviewDto.getFileName(),LocalDateTime.now(),reviewDto.getOriginalFileName(),reviewDto.getPriceScore(),reviewDto.getKindnessScore(),reviewDto.getEffectScore());

        return reviewRepository.save(entity);
    }

    public void delete(Long id) {
        ReviewEntity entity = reviewRepository.findById(id).orElseThrow(()-> new RuntimeException("존재하지 않는 리뷰입니다."));

        entity.changeDeleteState();
        entity.isDeleteYn();

        reviewRepository.save(entity);
    }
    public void updateImage(Long id){
    }
    public void deleteImage(Long id){
        ReviewEntity entity = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다."));
        String fileName = entity.getFileName();

        File file = new File(path + "\\" + fileName);

        if(file.exists()){
            file.delete();
        }
    }
    public ReviewDto getReview(Long id) {
        ReviewEntity entity = reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰입니다."));

        return ReviewDto.builder()
                .reviewId(entity.getReviewId())
                .petHospitalNum(entity.getPetHospitalEntity().getHospitalId())
                .petHospitalName(entity.getPetHospitalEntity().getHospitalName())
                .userNum(entity.getUserEntity().getIdx()) //수정 0207
                .content(entity.getContent())
                .score(entity.getScore())
                .createdAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .updatedAt(entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .fileName(entity.getFileName())
                .originalFileName(entity.getOriginalFileName())
                .build();
    }

    public Header<List<ReviewDto>> getReviewList(Pageable pageable, SearchCondition searchCondition, Long id) {

        List<ReviewDto> dtos = new ArrayList<>();
        Page<ReviewEntity> reviewEntities = reviewRepositoryCustom.findAllBySearchCondition(pageable, searchCondition, id);

        for (ReviewEntity entity : reviewEntities) {
            int rounded = (int)entity.getScore();

            ReviewDto dto = ReviewDto.builder()
                    .reviewId(entity.getReviewId())
                    .petHospitalNum(entity.getPetHospitalEntity().getHospitalId())
                    .userNum(entity.getUserEntity().getIdx()) // 수정 0207
                    .content(entity.getContent())
                    .score(rounded)
                    .priceScore(entity.getPriceScore())
                    .effectScore(entity.getEffectScore())
                    .kindnessScore(entity.getKindnessScore())
                    .userId(entity.getUserEntity().getUserId())
                    .createdAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .updatedAt(entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .build();
                dtos.add(dto);
            }
        Pagination pagination = new Pagination(
                (int) reviewEntities.getTotalElements()
                , pageable.getPageNumber() + 1
                , pageable.getPageSize()
                , 10
        );
        return Header.OK(dtos, pagination);
    }

    public Header<List<ReviewListWithHelpfulCount>> getReviewListWithHelpfulCount(Pageable pageable, Long hid, Long uid) {

        List<ReviewListWithHelpfulCount> dtos = new ArrayList<>();
        Page<ReviewListWithHelpfulCount> reviewListWithHelpfulCount = reviewRepositoryCustom.findAllWithHelpfulCount(pageable, hid, uid);


        for (ReviewListWithHelpfulCount dto1 : reviewListWithHelpfulCount) {
            // 점수 반올림
            int rounded = (int) Math.round(dto1.getScore());


            // 새로운 DTO 객체 생성
            ReviewListWithHelpfulCount dto2 = ReviewListWithHelpfulCount.builder()
                    .reviewId(dto1.getReviewId())
                    .petHospitalNum(dto1.getPetHospitalNum())
                    .userNum(dto1.getUserNum())
                    .content(dto1.getContent())
                    .score(rounded)
                    .priceScore(dto1.getPriceScore())
                    .effectScore(dto1.getEffectScore())
                    .kindnessScore(dto1.getKindnessScore())
                    .userId(dto1.getUserId())
                    .helpFul(dto1.getHelpFul())
                    .createdAt(dto1.getCreatedAt())
                    .updatedAt(dto1.getUpdatedAt())
                    .build();

            dtos.add(dto2);
        }

        Pagination pagination = new Pagination(
                (int) reviewListWithHelpfulCount.getTotalElements(),  // 총 아이템 개수
                pageable.getPageNumber() + 1,  // 현재 페이지 번호
                pageable.getPageSize(),  // 페이지 크기
                10  // 페이지네이션에서 보여줄 페이지 수
        );
        return Header.OK(dtos, pagination);
    }

    public Header<List<ReviewDto>> getUserReviewList(Pageable pageable, SearchCondition searchCondition, Long id) {

        List<ReviewDto> dtos = new ArrayList<>();
        Page<ReviewEntity> reviewEntities = reviewRepositoryCustom.findAllByUserId(pageable, searchCondition, id);
        for (ReviewEntity entity : reviewEntities) {

            int rounded = (int)entity.getScore();
            ReviewDto dto = ReviewDto.builder()
                    .reviewId(entity.getReviewId())
                    .petHospitalNum(entity.getPetHospitalEntity().getHospitalId())
                    .userNum(entity.getUserEntity().getIdx()) // 수정 0207
                    .content(entity.getContent())
                    .score(rounded)
                    .tmpScore(entity.getTmpScore())
                    .priceScore(entity.getPriceScore())
                    .kindnessScore(entity.getKindnessScore())
                    .effectScore(entity.getEffectScore())
                    .userId(entity.getUserEntity().getUserId())
                    .hosName(entity.getPetHospitalEntity().getHospitalName())
                    .approveYn(entity.isApproveYn())
                    .createdAt(entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .updatedAt(entity.getUpdatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                    .build();
            dtos.add(dto);
        }
        Pagination pagination = new Pagination(
                (int) reviewEntities.getTotalElements()
                , pageable.getPageNumber() + 1
                , pageable.getPageSize()
                , 10
        );
        return Header.OK(dtos, pagination);
    }
}
