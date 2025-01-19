package com.msproject.pet.entity;

import com.msproject.pet.service.UserService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("2. 유저정보 검색 후 비밀번호 비교")
    @Test
    void test_2(){
        String userId = "test_user";
        String userPw = "test_password";
        UserDetails user = userService.loadUserByUsername(userId);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, userPw);
        authenticationManager.authenticate(authenticationToken);

        assertThat(authenticationToken.getCredentials()).isEqualTo(userPw);

        System.out.println("getCredentials: " + authenticationToken.getCredentials());
        System.out.println("userPw: " + userPw);
    }


    @DisplayName("1. 유저 데이터 생성하기")
    @Test
    void test_1(){
        String encPassword = passwordEncoder.encode("qwer1234");

        UserEntity userEntity = UserEntity.builder()
                .userId("test_user1")
                .userPw(encPassword)
                .userName("테스트유저")
                .build();

        UserEntity savedUser = userRepository.save(userEntity);
        assertThat(userEntity.getUserId()).isEqualTo(savedUser.getUserId());
    }

    @Test
    public void insertUser(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            UserEntity user = UserEntity.builder()
                    .userId("user" + i)
                    .userPw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@email.com")
                    .deleteYn(false)
                    .social(false)
                    .build();

            user.addRole(UserRole.USER);

            if(i >= 90){
                user.addRole(UserRole.ADMIN);
            }
            userRepository.save(user);
        });

    }

    @Test
    public void testRead(){
        Optional<UserEntity> userEntity = userRepository.getWithRole("user100");
        UserEntity user = userEntity.orElseThrow();

        log.info(user);
        log.info(user.getRoleSet());

        user.getRoleSet().forEach(userRole -> log.info(userRole.name()));
    }

    @Commit
    @Test
    public void testUpdate(){
        String userId = "test_user@naver.com";
        String userPw = passwordEncoder.encode("54321");
        userRepository.updatePassword(userPw, userId);
    }

}