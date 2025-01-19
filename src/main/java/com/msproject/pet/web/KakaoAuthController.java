package com.msproject.pet.web;

import com.msproject.pet.security.CustomOAuth2UserService;
import com.msproject.pet.security.UserSecurityDTO;
import com.msproject.pet.service.KakaoAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth/kakao")
public class KakaoAuthController {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final KakaoAuthService kakaoAuthService;
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

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // KakaoAuthService에서 액세스 토큰을 받아옵니다.
        String accessToken = kakaoAuthService.getKakaoAccessToken(code);

        // 액세스 토큰을 사용하여 사용자 정보 가져오기
        UserSecurityDTO userSecurityDTO = kakaoAuthService.getUserInfo(accessToken);

        // 사용자 정보를 URL 파라미터로 변환
        //Long userIdLong = (Long) userInfo.get("id");
        //String userId = String.valueOf(userIdLong); // Long을 String으로 변환
        String email = userSecurityDTO.getUserId();
        String nickname = userSecurityDTO.getName();

        // URL 파라미터를 URL 인코딩
        try {
            //userId = URLEncoder.encode(userId, "UTF-8");
            email = URLEncoder.encode(email, "UTF-8");
            nickname = URLEncoder.encode(nickname, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 리디렉션 URL 지정 (사용자 정보를 쿼리 파라미터로 포함)
        String redirectUrl = String.format("http://localhost:8080/?userId=%s&nickname=%s",
                email, nickname);

        // 리디렉션 처리
        response.sendRedirect(redirectUrl);
    }

//    @GetMapping("/callback")
//    public void kakaoCallback(@AuthenticationPrincipal OAuth2UserRequest oAuth2UserRequest, HttpServletResponse response) throws IOException {
//        // Kakao에서 반환한 사용자 정보 가져오기
//        OAuth2User oAuth2User = customOAuth2UserService.loadUser(oAuth2UserRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
//        // Kakao 정보에서 이메일과 닉네임 추출
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        String email = (String) kakaoAccount.get("email");
//
//        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//        String nickname = (String) properties.get("nickname");
//
//        // 리디렉션 URL 지정 (사용자 정보를 쿼리 파라미터로 포함)
//        String redirectUrl = String.format("http://localhost:8080/?userId=%s&nickname=%s", email, nickname);
//
//        // 리디렉션 처리
//        response.sendRedirect(redirectUrl);
//    }
}
