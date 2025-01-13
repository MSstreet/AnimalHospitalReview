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
    private String social;

    public UserSecurityDTO(String userId, String password, String username, String phoneNum,
                           String zipCode, String addr, String detailAddr, String email, boolean deleteYn, String social,
                            Collection<? extends GrantedAuthority> authorities ) {
        super(username,password,authorities);

        this.userId = username;
        this.userPw = password;
    }
}
