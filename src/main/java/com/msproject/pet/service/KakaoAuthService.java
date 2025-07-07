package com.msproject.pet.service;

import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.entity.UserRepository;
import com.msproject.pet.entity.UserRole;
import com.msproject.pet.security.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

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
    private final WebClient webClient;
    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.client-secret}")
    private String clientSecret;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public String getKakaoAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        try {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
                    .queryParam("grant_type", "authorization_code")
                    .queryParam("client_id", clientId)
                    .queryParam("client_secret", clientSecret)
                    .queryParam("redirect_uri", redirectUri)
                    .queryParam("code", code);

            Map<String, Object> response = webClient.post()
                    .uri(uriBuilder.toUriString())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            return (String) response.get("access_token");
        } catch (Exception e) {
            log.error("[KakaoAuthService] getKakaoAccessToken error: {}", e.getMessage());
            throw new RuntimeException("카카오 토큰 발급 실패");
        }
    }

    public UserSecurityDTO getUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        try {
            Map<String, Object> userInfo = webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            Long userIdLong = (Long) userInfo.get("id");
            String userId = String.valueOf(userIdLong);
            String email = (String) ((Map<String, Object>) userInfo.get("kakao_account")).get("email");
            String nickname = (String) ((Map<String, Object>) userInfo.get("properties")).get("nickname");
            log.info("userId: " + userId);
            log.info("email: " + email);
            log.info("nickname: " + nickname);
            return generateDTO(email, nickname);
        } catch (Exception e) {
            log.error("[KakaoAuthService] getUserInfo error: {}", e.getMessage());
            throw new RuntimeException("카카오 사용자 정보 조회 실패");
        }
    }

    private UserSecurityDTO generateDTO(String email,String nickname) {
        Optional<UserEntity> result = userRepository.findByUserId(email);

        if (result.isEmpty()) {
            String tempPassword = generateSecureTempPassword();
            
            UserEntity user = UserEntity.builder()
                    .userId(email)
                    .userPw(passwordEncoder.encode(tempPassword))
                    .userName(nickname)
                    .social(true)
                    .deleteYn(false)
                    .build();
            user.addRole(UserRole.USER);
            userRepository.save(user);

            return new UserSecurityDTO(email, tempPassword, false, true, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
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
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*";
        
        StringBuilder password = new StringBuilder();
        
        password.append(upperCase.charAt((int) (Math.random() * upperCase.length())));
        password.append(lowerCase.charAt((int) (Math.random() * lowerCase.length())));
        password.append(numbers.charAt((int) (Math.random() * numbers.length())));
        password.append(specialChars.charAt((int) (Math.random() * specialChars.length())));
        
        String allChars = upperCase + lowerCase + numbers + specialChars;
        for (int i = 0; i < 6; i++) {
            password.append(allChars.charAt((int) (Math.random() * allChars.length())));
        }
        
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}
