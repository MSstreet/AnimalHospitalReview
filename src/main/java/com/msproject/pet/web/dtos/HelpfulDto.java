package com.msproject.pet.web.dtos;

import com.msproject.pet.entity.ReportEntity;
import lombok.Data;

@Data
public class HelpfulDto {

    private Long idx;
    private Long reviewNum;
    private Long userNum;
    private int helpFul;
    private String createdAt;
    private String updatedAt;

    public static ReportDto fromEntity(ReportEntity reportEntity) {
        return new ReportDto(
                reportEntity.getReportIdx(),
                reportEntity.getReviewEntity().getReviewId(),
                reportEntity.getUserEntity().getIdx(),
                reportEntity.getReportReason(),
                reportEntity.getReportDetail(),
                reportEntity.getDeleteFlag()
        );
    }
}
