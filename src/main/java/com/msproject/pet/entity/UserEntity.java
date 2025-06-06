package com.msproject.pet.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@ToString(exclude = "roleSet")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UserEntity extends BaseEntity{

    @Id
    @Column(name = "user_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String userId;
    private String userPw;
    private String userName;
    private String phoneNum;
    private String zipCode;
    private String addr;
    private String detailAddr;
    @ColumnDefault("false") //default 0
    private boolean social;
    private boolean deleteYn;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();

    public void addRole(UserRole userRole) {
        this.roleSet.add(userRole);
    }

    public void clearRoles() {
        this.roleSet.clear();
    }

    public void change(String userName, String phoneNum, String zipCode, String addr, String detailAddr){
        this.userName =userName;
        this.phoneNum = phoneNum;
        this.zipCode = zipCode;
        this.addr = addr;
        this.detailAddr = detailAddr;
    }

    public void joinUpdate(String userPw, String phoneNum, String zipCode, String addr, String detailAddr){
        this.userPw = userPw;
        this.phoneNum = phoneNum;
        this.zipCode = zipCode;
        this.addr = addr;
        this.detailAddr = detailAddr;
    }

    public void changePw(String userPw){
        this.userPw = userPw;
    }

}
