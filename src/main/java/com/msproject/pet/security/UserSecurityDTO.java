package com.msproject.pet.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class UserSecurityDTO  extends User {

    private String userId;
    private String userPw;
    private String userName;
    private String phoneNum;
    private String zipCode;
    private String addr;
    private String detailAddr;
    private String email;
    private boolean deleteYn;
    private boolean social;

    public UserSecurityDTO(String userId, String userPw, String userName, String phoneNum,
                           String zipCode, String addr, String detailAddr, String email,
                           boolean deleteYn, boolean social,
                           Collection<? extends GrantedAuthority> authorities) {
        super(userId, userPw, authorities);
        this.userId = userId;
        this.userPw = userPw;
        this.userName = userName;
        this.phoneNum = phoneNum;
        this.zipCode = zipCode;
        this.addr = addr;
        this.detailAddr = detailAddr;
        this.email = email;
        this.deleteYn = deleteYn;
        this.social = social;
    }
}
