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
//    @Value("${security.oauth2.client.registration.kakao.client-id}")
//    private String CLIENT_ID;
//    @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
//    private String REDIRECT_URI;

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

    @GetMapping("/callback1")
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

//        String email = userSecurityDTO.getUserId();
//        String nickname = userSecurityDTO.getUserName();
//        if(email != null && nickname != null) {
//            // URL 파라미터를 URL 인코딩
//            try {
//                email = URLEncoder.encode(email, "UTF-8");
//                nickname = URLEncoder.encode(nickname, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        // 리디렉션 URL 지정 (사용자 정보를 쿼리 파라미터로 포함)
//        String redirectUrl = String.format("http://localhost:8080/?userId=%s&nickname=%s",
//                email, nickname);
//        // 리디렉션 처리
//        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, Object>> kakaoUserInfoCallback(@RequestParam String email, @RequestParam String nickname) {

        String jwtToken = jwtUtil.createToken(email, nickname);     //accessToken 생성
        Map<String, Object> result = new HashMap<>();

        result.put("user_token", jwtToken);
        result.put("user_idx", userService.getUser(email).getIdx());

        return ResponseEntity.ok(result);
    }
}
