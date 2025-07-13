package com.msproject.pet.repository;

import com.msproject.pet.entity.PetHospitalEntity;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.web.dtos.PetHospitalListReviewCountDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.msproject.pet.entity.QPetHospitalEntity.petHospitalEntity;
import static com.msproject.pet.entity.QReviewEntity.reviewEntity;

@RequiredArgsConstructor
@Repository
public class PetHospitalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<PetHospitalListReviewCountDto> findAllBySearchConditionWithReviewCount(Pageable pageable, SearchCondition searchCondition) {
        // 기본 조건 설정
        BooleanExpression baseCondition = petHospitalEntity.operState.contains("정상");
        BooleanExpression searchExpr = buildSearchCondition(searchCondition.getSk(), searchCondition.getSv());
        
        // 전체 카운트 쿼리 (성능 최적화)
        long total = queryFactory
                .select(petHospitalEntity.count())
                .from(petHospitalEntity)
                .where(baseCondition, searchExpr)
                .fetchOne();

        // 메인 쿼리
        List<PetHospitalListReviewCountDto> results = queryFactory
                .select(Projections.bean(PetHospitalListReviewCountDto.class,
                        petHospitalEntity.hospitalId,
                        petHospitalEntity.hospitalName,
                        petHospitalEntity.sigunName,
                        petHospitalEntity.operState,
                        petHospitalEntity.hospitalNum,
                        petHospitalEntity.hospitalAddr,
                        reviewEntity.count().as("reviewCount")
                ))
                .from(petHospitalEntity)
                .leftJoin(reviewEntity).on(reviewEntity.petHospitalEntity.eq(petHospitalEntity)
                        .and(reviewEntity.deleteYn.eq(false))
                        .and(reviewEntity.approveYn.eq(true)))
                .where(baseCondition, searchExpr)
                .groupBy(petHospitalEntity.hospitalId, petHospitalEntity.hospitalName, 
                        petHospitalEntity.sigunName, petHospitalEntity.operState, 
                        petHospitalEntity.hospitalNum, petHospitalEntity.hospitalAddr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.score.avg().desc().nullsLast())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public PetHospitalListReviewCountDto findWithReviewCountById(Long id) {
        return queryFactory
                .select(Projections.bean(PetHospitalListReviewCountDto.class,
                        petHospitalEntity.hospitalId,
                        petHospitalEntity.hospitalName,
                        petHospitalEntity.sigunName,
                        petHospitalEntity.operState,
                        petHospitalEntity.hospitalNum,
                        petHospitalEntity.hospitalAddr,
                        petHospitalEntity.hosLatitude,
                        petHospitalEntity.hosLongitude,
                        reviewEntity.count().as("reviewCount")
                ))
                .from(petHospitalEntity)
                .leftJoin(reviewEntity).on(reviewEntity.petHospitalEntity.eq(petHospitalEntity)
                        .and(reviewEntity.deleteYn.eq(false))
                        .and(reviewEntity.approveYn.eq(true)))
                .where(petHospitalEntity.hospitalId.eq(id))
                .groupBy(petHospitalEntity.hospitalId, petHospitalEntity.hospitalName, 
                        petHospitalEntity.sigunName, petHospitalEntity.operState, 
                        petHospitalEntity.hospitalNum, petHospitalEntity.hospitalAddr,
                        petHospitalEntity.hosLatitude, petHospitalEntity.hosLongitude)
                .fetchOne();
    }

    public Page<PetHospitalEntity> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition) {
        // 기본 조건 설정
        BooleanExpression baseCondition = petHospitalEntity.operState.contains("정상");
        BooleanExpression searchExpr = buildSearchCondition(searchCondition.getSk(), searchCondition.getSv());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(petHospitalEntity.count())
                .from(petHospitalEntity)
                .where(baseCondition, searchExpr)
                .fetchOne();

        // 메인 쿼리
        List<PetHospitalEntity> results = queryFactory
                .selectFrom(petHospitalEntity)
                .where(baseCondition, searchExpr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(petHospitalEntity.hospitalId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<PetHospitalListReviewCountDto> findAllBySearchConditionWithReviewCountByAddress(Pageable pageable, SearchCondition searchCondition) {
        // 기본 조건 설정
        BooleanExpression baseCondition = petHospitalEntity.operState.contains("정상");
        BooleanExpression addressExpr = buildAddressSearchCondition(searchCondition.getSv());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(petHospitalEntity.count())
                .from(petHospitalEntity)
                .where(baseCondition, addressExpr)
                .fetchOne();

        // 메인 쿼리
        List<PetHospitalListReviewCountDto> results = queryFactory
                .select(Projections.bean(PetHospitalListReviewCountDto.class,
                        petHospitalEntity.hospitalId,
                        petHospitalEntity.hospitalName,
                        petHospitalEntity.sigunName,
                        petHospitalEntity.operState,
                        petHospitalEntity.hospitalNum,
                        petHospitalEntity.hospitalAddr,
                        reviewEntity.count().as("reviewCount")
                ))
                .from(petHospitalEntity)
                .leftJoin(reviewEntity).on(reviewEntity.petHospitalEntity.eq(petHospitalEntity)
                        .and(reviewEntity.deleteYn.eq(false))
                        .and(reviewEntity.approveYn.eq(true)))
                .where(baseCondition, addressExpr)
                .groupBy(petHospitalEntity.hospitalId, petHospitalEntity.hospitalName, 
                        petHospitalEntity.sigunName, petHospitalEntity.operState, 
                        petHospitalEntity.hospitalNum, petHospitalEntity.hospitalAddr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(petHospitalEntity.hospitalId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<PetHospitalListReviewCountDto> findAllBySearchConditionWithReviewCount1(Pageable pageable, SearchCondition searchCondition) {
        // 기본 조건 설정
        BooleanExpression baseCondition = petHospitalEntity.operState.contains("정상");
        BooleanExpression searchExpr = buildSearchCondition(searchCondition.getSk(), searchCondition.getSv());
        
        // 전체 카운트 쿼리 (성능 최적화)
        long total = queryFactory
                .select(petHospitalEntity.count())
                .from(petHospitalEntity)
                .where(baseCondition, searchExpr)
                .fetchOne();

        // 메인 쿼리 - 평균 점수도 함께 계산
        List<PetHospitalListReviewCountDto> results = queryFactory
                .select(Projections.bean(PetHospitalListReviewCountDto.class,
                        petHospitalEntity.hospitalId,
                        petHospitalEntity.hospitalName,
                        petHospitalEntity.sigunName,
                        petHospitalEntity.operState,
                        petHospitalEntity.hospitalNum,
                        petHospitalEntity.hospitalAddr,
                        reviewEntity.count().as("reviewCount"),
                        reviewEntity.score.avg().as("hospitalScore")
                ))
                .from(petHospitalEntity)
                .leftJoin(reviewEntity).on(reviewEntity.petHospitalEntity.eq(petHospitalEntity)
                        .and(reviewEntity.deleteYn.eq(false))
                        .and(reviewEntity.approveYn.eq(true)))
                .where(baseCondition, searchExpr)
                .groupBy(petHospitalEntity.hospitalId, petHospitalEntity.hospitalName, 
                        petHospitalEntity.sigunName, petHospitalEntity.operState, 
                        petHospitalEntity.hospitalNum, petHospitalEntity.hospitalAddr)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.score.avg().desc().nullsLast())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression buildSearchCondition(String searchKey, String searchValue) {
        if (!StringUtils.hasLength(searchValue)) {
            return null;
        }

        return switch (searchKey) {
            case "author" -> petHospitalEntity.hospitalName.contains(searchValue);
            case "title" -> petHospitalEntity.hospitalAddr.contains(searchValue);
            default -> null;
        };
    }

    private BooleanExpression buildAddressSearchCondition(String searchValue) {
        return StringUtils.hasLength(searchValue) 
                ? petHospitalEntity.hospitalAddr.contains(searchValue) 
                : null;
    }
}
