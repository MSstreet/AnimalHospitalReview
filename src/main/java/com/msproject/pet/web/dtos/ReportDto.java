package com.msproject.pet.web.dtos;

import com.msproject.pet.entity.PetHospitalEntity;
import com.msproject.pet.entity.UserEntity;
import lombok.Data;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Data
public class ReportDto {

    private Long reportIdx;
    private PetHospitalEntity reviewEntity;
    private UserEntity userEntity;
    private int reportReason;
    private int reportDetail;
}
