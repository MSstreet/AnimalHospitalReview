package com.msproject.pet.repository;

import com.msproject.pet.entity.BoardReply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.msproject.pet.entity.QBoardReply.boardReply;

@RequiredArgsConstructor
@Repository
public class BoardReplyRepositoryCustom {
    
    private final JPAQueryFactory queryFactory;
    
    public Page<BoardReply> findAllByBoardIdAndParentIsNull(Pageable pageable, Long boardId) {
        // 기본 조건 설정
        var baseCondition = boardReply.boardEntity.idx.eq(boardId)
                .and(boardReply.deleteYn.eq(false))
                .and(boardReply.parent.isNull());
        
        // 전체 카운트 쿼리 (성능 최적화)
        long total = queryFactory
                .select(boardReply.count())
                .from(boardReply)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<BoardReply> results = queryFactory
                .selectFrom(boardReply)
                .where(baseCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardReply.replyIdx.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
    
    public Page<BoardReply> findAllByBoardIdAndParentIsNotNull(Pageable pageable, Long boardId) {
        // 기본 조건 설정
        var baseCondition = boardReply.boardEntity.idx.eq(boardId)
                .and(boardReply.deleteYn.eq(false))
                .and(boardReply.parent.isNotNull());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(boardReply.count())
                .from(boardReply)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<BoardReply> results = queryFactory
                .selectFrom(boardReply)
                .where(baseCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardReply.replyIdx.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<BoardReply> findAllBySearchCondition(Pageable pageable, Long boardId) {
        // 기본 조건 설정 - 부모 댓글만 조회 (대댓글 제외)
        var baseCondition = boardReply.boardEntity.idx.eq(boardId)
                .and(boardReply.deleteYn.eq(false))
                .and(boardReply.parent.isNull());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(boardReply.count())
                .from(boardReply)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<BoardReply> results = queryFactory
                .selectFrom(boardReply)
                .where(baseCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardReply.replyIdx.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }

    public Page<BoardReply> findAllBySearchCondition1(Pageable pageable, Long boardId) {
        // 기본 조건 설정 - 대댓글만 조회
        var baseCondition = boardReply.boardEntity.idx.eq(boardId)
                .and(boardReply.deleteYn.eq(false))
                .and(boardReply.parent.isNotNull());
        
        // 전체 카운트 쿼리
        long total = queryFactory
                .select(boardReply.count())
                .from(boardReply)
                .where(baseCondition)
                .fetchOne();

        // 메인 쿼리
        List<BoardReply> results = queryFactory
                .selectFrom(boardReply)
                .where(baseCondition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardReply.replyIdx.desc())
                .fetch();

        return new PageImpl<>(results, pageable, total);
    }
}
