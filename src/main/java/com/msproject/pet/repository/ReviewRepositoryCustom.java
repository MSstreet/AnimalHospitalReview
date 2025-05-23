package com.msproject.pet.repository;

import com.msproject.pet.entity.ReviewEntity;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.web.dtos.ReviewListWithHelpfulCount;
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

import static com.msproject.pet.entity.QReviewEntity.reviewEntity;
import static com.msproject.pet.entity.QHelpfulEntity.helpfulEntity;
import static com.msproject.pet.entity.QUserEntity.userEntity;

@RequiredArgsConstructor
@Repository
public class ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ReviewEntity> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition, Long id) {


        JPAQuery<ReviewEntity> query = queryFactory.selectFrom(reviewEntity)
                .where(reviewEntity.petHospitalEntity.hospitalId.eq(id), reviewEntity.deleteYn.eq(false), reviewEntity.approveYn.eq(true));

        long total = query.stream().count();   //여기서 전체 카운트 후 아래에서 조건작업

        List<ReviewEntity> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<ReviewListWithHelpfulCount> findAllWithHelpfulCount(Pageable pageable, Long hid, Long uid) {

        JPAQuery<ReviewEntity> query = queryFactory.selectFrom(reviewEntity)
                .leftJoin(helpfulEntity).on(helpfulEntity.reviewEntity.eq(reviewEntity))
                .leftJoin(userEntity).on(helpfulEntity.userEntity.eq(userEntity));

        query.where(reviewEntity.petHospitalEntity.hospitalId.eq(hid),reviewEntity.deleteYn.eq(false), reviewEntity.approveYn.eq(true));

        query.groupBy(reviewEntity,helpfulEntity);

        long total = query.stream().count();   //여기서 전체 카운트 후 아래에서 조건작업

        JPAQuery<ReviewListWithHelpfulCount> dtoJPAQuery = query.select(Projections.bean(ReviewListWithHelpfulCount.class,
                reviewEntity.reviewId,
                reviewEntity.petHospitalEntity.hospitalId,
                reviewEntity.hospitalName,
                reviewEntity.userEntity.idx,
                reviewEntity.content,
                reviewEntity.hospitalName,
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
                reviewEntity.count().as("helpfulCount")
                ));

        List<ReviewListWithHelpfulCount> dtoList =dtoJPAQuery.fetch();

        return new PageImpl<>(dtoList, pageable, total);
    }


    public Page<ReviewEntity> findAllByUserId(Pageable pageable, SearchCondition searchCondition, Long id) {

        JPAQuery<ReviewEntity> query = queryFactory.selectFrom(reviewEntity)
                .where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()),  reviewEntity.deleteYn.eq(false))
                .where(reviewEntity.userEntity.idx.eq(id));

        long total = query.stream().count();   //여기서 전체 카운트 후 아래에서 조건작업

        List<ReviewEntity> results = query
                .where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reviewEntity.reviewId.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }


    private BooleanExpression searchKeywords(String sk, String sv) {

        if ("name".equals(sk)) {
            if (StringUtils.hasLength(sv)) {
                return reviewEntity.content.contains(sv);
            }
        }
        return null;
    }

    public double getReviewAvg(Long id) {
        JPAQuery<Double> score = queryFactory.select(reviewEntity.score.avg()).where(reviewEntity.petHospitalEntity.hospitalId.eq(id).and(reviewEntity.deleteYn.eq(false)).and(reviewEntity.approveYn.eq(true))).from(reviewEntity);

        double results;
        if(score.fetchOne() == null){
            return 0;
        }else{
            results = score.fetchOne();
            return results;
        }

    }
}
