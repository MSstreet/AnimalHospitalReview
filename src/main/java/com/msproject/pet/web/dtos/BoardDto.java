package com.msproject.pet.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardDto implements Serializable {

    private Long idx;
    private String title;
    private String contents;
    private String author;
    private Long userIdx;
    private String userId;
    private boolean deleteYn;
    private String createdAt;
    private String updatedAt;
}
