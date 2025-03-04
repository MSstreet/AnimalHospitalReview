package com.msproject.pet.web.dtos;

import com.msproject.pet.entity.HelpfulEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HelpfulDto {

    private Long idx;
    private Long reviewNum;
    private Long userNum;
    private int helpFul;
    private String createdAt;
    private String updatedAt;

    public static HelpfulDto fromEntity(HelpfulEntity helpfulEntity) {
        return new HelpfulDto(
                helpfulEntity.getIdx(),
                helpfulEntity.getReviewEntity().getReviewId(),
                helpfulEntity.getUserEntity().getIdx(),
                helpfulEntity.getHelpFul(),
                helpfulEntity.getCreatedAt().toString(),
                helpfulEntity.getUpdatedAt().toString()
        );
    }
}
