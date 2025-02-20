package com.msproject.pet.entity;

import com.msproject.pet.web.dtos.ReportDto;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_idx")
    private Long reportIdx;
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewEntity reviewEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;
    private int reportReason;
    private String reportDetail;
    private int deleteFlag;

    public static ReportEntity of(ReportDto reportDto,ReviewEntity reviewEntity,UserEntity userEntity) {
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.reviewEntity = reviewEntity;
        reportEntity.userEntity = userEntity;
        reportEntity.setReportReason(reportDto.getReportReason());
        reportEntity.setReportDetail(reportDto.getReportDetail());
        reportEntity.setDeleteFlag(reportDto.getDeleteFlag());

        return reportEntity;
    }

}
