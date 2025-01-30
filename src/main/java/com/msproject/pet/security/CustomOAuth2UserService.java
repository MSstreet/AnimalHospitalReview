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
            UserEntity user = UserEntity.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode("1111"))
                    //.email(email)
                    .social(true)
                    .deleteYn(false)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);

            UserSecurityDTO userSecurityDTO = new UserSecurityDTO(email, "1111", false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            userSecurityDTO.setProps(params);

            return userSecurityDTO;
        } else {
            UserEntity user = result.get();
            UserSecurityDTO userSecurityDTO =
                    new UserSecurityDTO(
                            user.getUserId(),
                            user.getUserPw(),
                            user.getUserName(),
                            user.getPhoneNum(),
                            user.getZipCode(),
                            user.getAddr(),
                            user.getDetailAddr(),
                            //user.getEmail(),
                            user.isDeleteYn(),
                            user.isSocial(),
                            user.getRoleSet()
                                    .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name())).collect(Collectors.toList())
                    );
            return userSecurityDTO;
        }
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
