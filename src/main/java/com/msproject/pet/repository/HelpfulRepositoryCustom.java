package com.msproject.pet.repository;

import com.msproject.pet.entity.HelpfulEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.msproject.pet.entity.QHelpfulEntity.helpfulEntity;

@RequiredArgsConstructor
@Repository
public class HelpfulRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public HelpfulEntity findOneHelpful(Long userId, Long reviewId) {

        JPAQuery<HelpfulEntity> query = queryFactory.selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId).and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)));

        HelpfulEntity results = query.fetchOne();

        return results;
    }
}
