package com.msproject.pet.web.dtos;

import lombok.Data;

@Data
public class UserJoinDto {

    private String userId;
    private String userPw;
    private String userName;
    private String phoneNum;
    private String zipCode;
    private String addr;
    private String detailAddr;
    private boolean deleteYn;
    private boolean social;
}
