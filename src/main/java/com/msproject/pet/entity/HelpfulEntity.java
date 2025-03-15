package com.msproject.pet.entity;

import com.msproject.pet.web.dtos.HelpfulDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Helpful")
@Entity
public class HelpfulEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewEntity reviewEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private UserEntity userEntity;
    private int helpFul;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HelpfulEntity of(HelpfulDto helpfulDto, ReviewEntity reviewEntity, UserEntity userEntity) {
        HelpfulEntity helpfulEntity = new HelpfulEntity();
        helpfulEntity.reviewEntity = reviewEntity;
        helpfulEntity.userEntity = userEntity;
        helpfulEntity.setHelpFul(helpfulDto.getHelpFul());
        return helpfulEntity;
    }

    public void changeHelpfulState(int helpFul){
        this.helpFul = helpFul;
    }
}
