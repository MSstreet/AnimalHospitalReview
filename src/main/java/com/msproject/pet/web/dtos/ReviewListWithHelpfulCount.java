package com.msproject.pet.web.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewListWithHelpfulCount {

    private Long reviewId;
    private Long petHospitalNum;
    private String petHospitalName;
    private Long userNum;
    private String userId;
    private String content;
    private String hosName;
    private int priceScore;
    private int kindnessScore;
    private int effectScore;
    private float score;
    private float tmpScore;
    private boolean deleteYn;
    private String createdAt;
    private String updatedAt;
    private MultipartFile file;
    private String fileName;
    private String originalFileName;
    private boolean approveYn;
    private Long helpfulCount;
}
