package com.msproject.pet.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetHospitalDto {

    private Long hospitalId;
    private String hospitalName;
    private String hospitalNum;
    private String hospitalAddr; // 도로명 주소
    private String sigunName;
    private String operState;
    private BigDecimal hosLatitude ;
    private BigDecimal hosLongitude;
}
