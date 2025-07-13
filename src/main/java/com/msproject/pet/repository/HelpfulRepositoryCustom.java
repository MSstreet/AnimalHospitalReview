package com.msproject.pet.repository;

import com.msproject.pet.entity.HelpfulEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.msproject.pet.entity.QHelpfulEntity.helpfulEntity;

@RequiredArgsConstructor
@Repository
public class HelpfulRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public long getHelpfulCount(Long reviewId) {
        return queryFactory
                .select(helpfulEntity.count())
                .from(helpfulEntity)
                .where(helpfulEntity.reviewEntity.reviewId.eq(reviewId)
                        .and(helpfulEntity.helpFul.eq(1)))
                .fetchOne();
    }

    public long getUnHelpfulCount(Long reviewId) {
        return queryFactory
                .select(helpfulEntity.count())
                .from(helpfulEntity)
                .where(helpfulEntity.reviewEntity.reviewId.eq(reviewId)
                        .and(helpfulEntity.helpFul.eq(2)))
                .fetchOne();
    }

    public boolean isHelpfulExists(Long userId, Long reviewId) {
        return queryFactory
                .selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId)
                        .and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)))
                .fetchFirst() != null;
    }

    public HelpfulEntity findHelpfulByUserAndReview(Long userId, Long reviewId) {
        return queryFactory
                .selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId)
                        .and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)))
                .fetchOne();
    }

    // 누락된 메서드들 추가
    public HelpfulEntity findOneHelpful(Long userId, Long reviewId) {
        return queryFactory
                .selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId)
                        .and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)))
                .fetchOne();
    }

    public boolean chkHelpful(Long userId, Long reviewId) {
        return queryFactory
                .selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId)
                        .and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)))
                .fetchFirst() != null;
    }
}
