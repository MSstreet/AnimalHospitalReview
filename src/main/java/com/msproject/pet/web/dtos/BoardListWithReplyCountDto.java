package com.msproject.pet.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardListWithReplyCountDto {

    private Long idx;
    private String title;
    private String contents;
    private String author;
    private Long userIdx;
    private String userId;
    private Long replyCount;
    private String createdAt1;
    private String updatedAt1;
    private boolean deleteYn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
