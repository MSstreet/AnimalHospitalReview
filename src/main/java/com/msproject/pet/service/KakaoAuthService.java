package com.msproject.pet.service;

import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.entity.UserRepository;
import com.msproject.pet.entity.UserRole;
import com.msproject.pet.security.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Log4j2
@RequiredArgsConstructor
@Service
public class KakaoAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

//    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;
//    @Value("${security.oauth2.client.registration.kakao.client-id}")
//    private String CLIENT_ID;
//    @Value("${security.oauth2.client.registration.kakao.client-secret}")
//    private String SECRET_KEY;
//    @Value("${security.oauth2.client.provider.kakao.token-uri}")
//    private String TOKEN_URI;


    public String getKakaoAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        String clientId = "409b3fb04dd78999f86c8dbc4a19372a";
        String clientSecret = "hCjNC6r9e9w8N6an8eOBxiWu2ZXP5en0";
        String redirectUri = "http://localhost:8081/oauth/kakao/callback";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code);

        ResponseEntity<Map> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                null,
                Map.class
        );

        return (String) response.getBody().get("access_token");

    }

    public UserSecurityDTO getUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, Map.class);

        Map<String, Object> userInfo = response.getBody();

        Long userIdLong = (Long) userInfo.get("id");
        String userId = String.valueOf(userIdLong); // Long을 String으로 변환
        String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
        String nickname = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");

        log.info("userId: " + userId);
        log.info("email: " + email);
        log.info("nickname: " + nickname);

        return generateDTO(email);
    }

    private UserSecurityDTO generateDTO(String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);

        if (result.isEmpty()) {
            UserEntity user = UserEntity.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .deleteYn(false)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);

            return new UserSecurityDTO(email, "1111", email, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
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
                    user.getEmail(),
                    user.isDeleteYn(),
                    user.isSocial(),
                    user.getRoleSet()
                            .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name())).collect(Collectors.toList())
            );
        }
    }
}
