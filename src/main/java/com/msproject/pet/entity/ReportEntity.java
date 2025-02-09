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
    private PetHospitalEntity reviewEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    private int reportReason;
    private int reportDetail;

    public static ReportEntity of(ReportDto reportDto){
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setReportReason(reportDto.getReportReason());
        reportEntity.setReportDetail(reportDto.getReportDetail());
        return reportEntity;
    }

}
