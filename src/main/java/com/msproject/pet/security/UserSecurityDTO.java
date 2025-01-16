package com.msproject.pet.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
public class UserSecurityDTO  extends User implements OAuth2User {

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

    private Map<String, Object> props;

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

    @Override
    public Map<String, Object> getAttributes() {
     return this.getProps();
    }

    @Override
    public String getName(){
        return this.userId;
    }
}
