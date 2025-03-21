package com.msproject.pet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;


import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="BOARD")
@Entity
public class BoardEntity {

    @Id
    @Column(name = "board_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private String contents;
    private String author; // 삭제해도 됨

    @ManyToOne(fetch = FetchType.LAZY)
    //@OnDelete(action= OnDeleteAction.CASCADE)
    private UserEntity userEntity;
    @ColumnDefault("false")
    private boolean deleteYn;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void changeState(){
        this.deleteYn = true;
    }

    public void changeDeleteState() {
        this.deleteYn = true;
    }
}
