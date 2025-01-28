package com.msproject.pet.web;

import com.msproject.pet.security.CustomOAuth2UserService;
import com.msproject.pet.security.UserSecurityDTO;
import com.msproject.pet.service.KakaoAuthService;
import com.msproject.pet.service.UserService;
import com.msproject.pet.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final KakaoAuthService kakaoAuthService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private static final String CLIENT_ID = "409b3fb04dd78999f86c8dbc4a19372a"; // 카카오 REST API 키
    private static final String REDIRECT_URI = "http://localhost:8081/oauth/kakao/callback"; // 리디렉션 URI

    @GetMapping("/login-url")
    public String getKakaoLoginUrl() {
        String authorizationUri = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                CLIENT_ID, REDIRECT_URI
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
}
