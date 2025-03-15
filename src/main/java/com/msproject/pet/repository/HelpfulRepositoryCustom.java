package com.msproject.pet.repository;

import com.msproject.pet.entity.HelpfulEntity;
import com.msproject.pet.entity.WishEntity;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.msproject.pet.entity.QHelpfulEntity.helpfulEntity;
import static com.msproject.pet.entity.QWishEntity.wishEntity;

@RequiredArgsConstructor
@Repository
public class HelpfulRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public long getHelpfulCount(Long id) {
        JPAQuery<HelpfulEntity> query = queryFactory.selectFrom(helpfulEntity)
                .where(helpfulEntity.reviewEntity.reviewId.eq(id),helpfulEntity.helpFul.eq(1));

        return query.stream().count();
    }

    public long getUnHelpfulCount(Long id) {
        JPAQuery<HelpfulEntity> query = queryFactory.selectFrom(helpfulEntity)
                .where(helpfulEntity.reviewEntity.reviewId.eq(id),helpfulEntity.helpFul.eq(2));

        return query.stream().count();
    }

    public boolean helpfulChk(Long userId, Long reviewId) {

        JPAQuery<HelpfulEntity> query = queryFactory.selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId).and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)));

        HelpfulEntity results = query.fetchOne();

        if(results != null) {
            return true;
        }

        return false;
    }

    public HelpfulEntity findOneHelpful(Long userId, Long reviewId) {
        JPAQuery<HelpfulEntity> query = queryFactory.selectFrom(helpfulEntity)
                .where(helpfulEntity.userEntity.idx.eq(userId).and(helpfulEntity.reviewEntity.reviewId.eq(reviewId)));

        HelpfulEntity results = query.fetchOne();

        return results;
    }
}
