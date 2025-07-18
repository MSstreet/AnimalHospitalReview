package com.msproject.pet.web;

import com.msproject.pet.security.CustomOAuth2UserService;
import com.msproject.pet.security.UserSecurityDTO;
import com.msproject.pet.service.KakaoAuthService;
import com.msproject.pet.service.UserService;
import com.msproject.pet.util.JwtUtil;
import com.msproject.pet.web.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/oauth/kakao")
public class KakaoAuthController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @GetMapping("/login-url")
    public String getKakaoLoginUrl() {
        String authorizationUri = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                clientId, redirectUri
        );
        return authorizationUri;
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // KakaoAuthService에서 액세스 토큰을 받아옵니다.
        String accessToken = kakaoAuthService.getKakaoAccessToken(code);

        // 액세스 토큰을 사용하여 사용자 정보 가져오기
        UserSecurityDTO userSecurityDTO = kakaoAuthService.getUserInfo(accessToken);

        String jwtToken = jwtUtil.createToken(userSecurityDTO.getUsername(), userSecurityDTO.getUsername());     //accessToken 생성
        Map<String, Object> result = new HashMap<>();
        result.put("user_id", userSecurityDTO.getUserId());
        result.put("user_name", userSecurityDTO.getUserName());
        result.put("user_token", jwtToken);
        result.put("user_role", userSecurityDTO.getAuthorities().stream().findFirst().get().getAuthority());
        result.put("user_idx",userService.getUser(userSecurityDTO.getUserId()).getIdx());

        return ResponseEntity.ok(result);

    }

    @PostMapping("/update")
    public void kakaoUserInfoUpdate(@RequestBody UserDto userDto){
        userService.update(userDto);
    }
}
