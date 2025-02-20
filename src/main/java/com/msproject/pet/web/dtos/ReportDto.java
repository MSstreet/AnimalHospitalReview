package com.msproject.pet.web.dtos;

import com.msproject.pet.entity.ReportEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportDto {

    private Long reportIdx;
    private Long reviewNum;
    private Long userNum;
    private int reportReason;
    private String reportDetail;
    private int deleteFlag;

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
