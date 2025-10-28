package com.msproject.pet.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PetHospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hospitalId;
    @Column(name = "hospital_name", length = 2000)
    private String hospitalName;
    private String sigunName;
    private String operState;
    @Column(name = "hospital_num", length = 2000)
    private String hospitalNum;
    @Column(name = "hospital_addr", length = 2000)
    private String hospitalAddr; // 도로명 주소
    private BigDecimal hosLatitude ;
    private BigDecimal hosLongitude;
    public void change(String hospitalName, String hospitalNum, String hospitalAddr, String sigunName){
        this.hospitalName = hospitalName;
        this.hospitalNum = hospitalNum;
        this.hospitalAddr = hospitalAddr;
        this.sigunName = sigunName;
    }
}
