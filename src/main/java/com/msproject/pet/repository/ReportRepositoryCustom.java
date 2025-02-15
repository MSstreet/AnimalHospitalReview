package com.msproject.pet.repository;

import com.msproject.pet.entity.NoticeBoard;
import com.msproject.pet.entity.ReportEntity;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.web.dtos.ReportDto;
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

import static com.msproject.pet.entity.QNoticeBoard.noticeBoard;
import static com.msproject.pet.entity.QReportEntity.reportEntity;

@RequiredArgsConstructor
@Repository
public class ReportRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<ReportEntity> findAllBySearchCondition(Pageable pageable, SearchCondition searchCondition) {

        JPAQuery<ReportEntity> query = queryFactory.selectFrom(reportEntity)
                .where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()),reportEntity.display_yn.eq(true));

        long total = query.stream().count();   //여기서 전체 카운트 후 아래에서 조건작업

        List<ReportEntity> results = query
                .where(searchKeywords(searchCondition.getSk(), searchCondition.getSv()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(reportEntity.reportIdx.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression searchKeywords(String sk, String sv) {
        if ("title".equals(sk)) {
            if(StringUtils.hasLength(sv)) {
                return reportEntity.title.contains(sv);
            }
        } else if ("contents".equals(sk)) {
            if(StringUtils.hasLength(sv)) {
                return reportEntity.contents.contains(sv);
            }
        }
        return null;
    }
}
