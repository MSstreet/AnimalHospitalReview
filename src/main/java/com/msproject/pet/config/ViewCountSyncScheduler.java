package com.msproject.pet.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.msproject.pet.entity.QBoardEntity.boardEntity;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCountSyncScheduler {

    private final RedisTemplate<String, String> redisTemplate;
    private final JPAQueryFactory queryFactory; // or BoardRepository

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다
    @Transactional
    public void syncViewCounts() {
        Set<String> keys = redisTemplate.keys("view:board:*");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {
            try {
                Long boardId = Long.parseLong(key.split(":")[2]);

                String value = redisTemplate.opsForValue().get(key);
                if (value == null) continue;

                Long redisCount = Long.parseLong(value);

                // DB에 viewCount 누적
                queryFactory.update(boardEntity)
                        .set(boardEntity.viewCount, boardEntity.viewCount.add(redisCount))
                        .where(boardEntity.idx.eq(boardId))
                        .execute();

                // Redis 키 삭제
                redisTemplate.delete(key);

            } catch (Exception e) {
                log.error("[ViewCount Sync] 실패 key: {}", key, e);
            }
        }
    }
}
