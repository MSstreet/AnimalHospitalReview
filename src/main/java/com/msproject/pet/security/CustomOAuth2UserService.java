package com.msproject.pet.security;

import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.entity.UserRepository;
import com.msproject.pet.entity.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("userRequest: " + userRequest);
        log.info("oauth2 user..............................................");
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("client name: " + clientName);
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        switch(clientName){
            case "kakao":
                email = getKakaoEmail(attributes);
                break;
        }

        log.info("email: " + email);
        return generateDTO(email, attributes);
    }

    private UserSecurityDTO generateDTO(String email, Map<String,Object> params) {
        Optional<UserEntity> result = userRepository.findByUserId(email);

        if (result.isEmpty()) {
            String tempPassword = generateSecureTempPassword();
            
            UserEntity user = UserEntity.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode(tempPassword))
                    .social(true)
                    .deleteYn(false)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);

            UserSecurityDTO userSecurityDTO = new UserSecurityDTO(email, tempPassword, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            userSecurityDTO.setProps(params);

            return userSecurityDTO;
        } else {
            UserEntity user = result.get();
            return new UserSecurityDTO(
                    user.getUserId(),
                    user.getUserPw(),
                    user.getUserName(),
                    user.getPhoneNum(),
                    user.getZipCode(),
                    user.getAddr(),
                    user.getDetailAddr(),

                    user.isDeleteYn(),
                    user.isSocial(),
                    user.getRoleSet()
                            .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name())).collect(Collectors.toList())
            );
        }
    }

    private String generateSecureTempPassword() {
        // 보안성 높은 임시 비밀번호 생성 (대문자, 소문자, 숫자, 특수문자 포함)
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*";
        
        StringBuilder password = new StringBuilder();
        
        // 각 카테고리에서 최소 1개씩 포함
        password.append(upperCase.charAt((int) (Math.random() * upperCase.length())));
        password.append(lowerCase.charAt((int) (Math.random() * lowerCase.length())));
        password.append(numbers.charAt((int) (Math.random() * numbers.length())));
        password.append(specialChars.charAt((int) (Math.random() * specialChars.length())));
        
        // 나머지 6자리는 모든 문자에서 랜덤 선택
        String allChars = upperCase + lowerCase + numbers + specialChars;
        for (int i = 0; i < 6; i++) {
            password.append(allChars.charAt((int) (Math.random() * allChars.length())));
        }
        
        // 문자열을 섞어서 더 랜덤하게 만듦
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }

    private String getKakaoEmail(Map<String, Object> attributes){
        log.info("KAKAO=================================================");

        Object value = attributes.get("account_email");
        log.info("value: " + value);

        LinkedHashMap accountMap = (LinkedHashMap)value;
        String email = (String)accountMap.get("email");
        log.info("email: " + email);

        return email;
    }
}
