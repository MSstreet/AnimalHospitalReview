package com.msproject.pet.repository;

import com.msproject.pet.entity.ReviewEntity;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.web.dtos.ReviewListWithHelpfulCount;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.msproject.pet.entity.QReviewEntity.reviewEntity;
import static com.msproject.pet.entity.QHelpfulEntity.helpfulEntity;
import static com.msproject.pet.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ReviewEntity> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition, Long hospitalId) {
        // 기본 조건 설정
        BooleanExpression baseCondition = reviewEntity.petHospitalEntity.hospitalId.eq(hospitalId)
                .and(reviewEntity.deleteYn.eq(false))
                .and(reviewEntity.approveYn.eq(true));
        
        // 전체 카운트 쿼리 (성능 최적화)
        long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<ReviewEntity> results = queryFactory
                .selectFrom(reviewEntity)
                .where(baseCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<ReviewListWithHelpfulCount> findAllWithHelpfulCount(Pageable pageable, Long hospitalId, Long userId) {
        // 기본 조건 설정
        BooleanExpression baseCondition = reviewEntity.petHospitalEntity.hospitalId.eq(hospitalId)
                .and(reviewEntity.deleteYn.eq(false))
                .and(reviewEntity.approveYn.eq(true));
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<ReviewListWithHelpfulCount> results = queryFactory
                .select(Projections.bean(ReviewListWithHelpfulCount.class,
                        reviewEntity.reviewId,
                        reviewEntity.petHospitalEntity.hospitalId,
                        reviewEntity.hospitalName,
                        reviewEntity.userEntity.idx,
                        reviewEntity.content,
                        reviewEntity.priceScore,
                        reviewEntity.kindnessScore,
                        reviewEntity.effectScore,
                        reviewEntity.score,
                        reviewEntity.tmpScore,
                        reviewEntity.deleteYn,
                        reviewEntity.createdAt,
                        reviewEntity.updatedAt,
                        reviewEntity.fileName,
                        reviewEntity.originalFileName,
                        reviewEntity.approveYn,
                        helpfulEntity.helpFul,
                        helpfulEntity.count().as("helpfulCount")
                ))
                .from(reviewEntity)
                .leftJoin(helpfulEntity).on(helpfulEntity.reviewEntity.eq(reviewEntity))
                .leftJoin(userEntity).on(helpfulEntity.userEntity.eq(userEntity))
                .where(baseCondition)
                .groupBy(reviewEntity.reviewId, reviewEntity.petHospitalEntity.hospitalId, 
                        reviewEntity.hospitalName, reviewEntity.userEntity.idx, reviewEntity.content,
                        reviewEntity.priceScore, reviewEntity.kindnessScore, reviewEntity.effectScore,
                        reviewEntity.score, reviewEntity.tmpScore, reviewEntity.deleteYn,
                        reviewEntity.createdAt, reviewEntity.updatedAt, reviewEntity.fileName,
                        reviewEntity.originalFileName, reviewEntity.approveYn, helpfulEntity.helpFul)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<ReviewEntity> findAllByUserId(Pageable pageable, SearchCondition searchCondition, Long userId) {
        // 기본 조건 설정
        BooleanExpression baseCondition = reviewEntity.userEntity.idx.eq(userId)
                .and(reviewEntity.deleteYn.eq(false));
        BooleanExpression searchExpr = buildSearchCondition(searchCondition.getSk(), searchCondition.getSv());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(reviewEntity.count())
                .from(reviewEntity)
                .where(baseCondition, searchExpr)
                .fetchOne();

        // 메인 쿼리
        List<ReviewEntity> results = queryFactory
                .selectFrom(reviewEntity)
                .where(baseCondition, searchExpr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public double getReviewAverageScore(Long hospitalId) {
        Double averageScore = queryFactory
                .select(reviewEntity.score.avg())
                .from(reviewEntity)
                .where(reviewEntity.petHospitalEntity.hospitalId.eq(hospitalId)
                        .and(reviewEntity.deleteYn.eq(false))
                        .and(reviewEntity.approveYn.eq(true)))
                .fetchOne();

        return averageScore != null ? averageScore : 0.0;
    }

    private BooleanExpression buildSearchCondition(String searchKey, String searchValue) {
        if (!StringUtils.hasLength(searchValue)) {
            return null;
        }

        return "name".equals(searchKey) 
                ? reviewEntity.content.contains(searchValue) 
                : null;
    }
}
