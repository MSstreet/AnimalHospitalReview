package com.msproject.pet.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.msproject.pet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String uri = request.getRequestURI();

            // 인증이 필요 없는 엔드포인트 화이트리스트
            String[] whiteList = {
                "/api/oauth/kakao/user-info",
                "/api/user/login",
                "/api/user/join",
                "/api/user/check",
                "/api/user/check/mail",
                "/api/user/find/pw",
                "/api/user/find",
                "/api/board/list",
                "/api/hospital/list",
                "/api/hospital/detail",
                "/api/hospital/list1",
                "/api/notice/list"
            };

            for (String white : whiteList) {
                if (uri.equals(white)) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }

            // 인증이 필요한 엔드포인트
            String token = parseJwt(request);
            if (token == null) {
                response.sendError(403);    //accessDenied
            } else {
                DecodedJWT tokenInfo = jwtUtil.decodeToken(token);
                if (tokenInfo != null) {
                    String userId = tokenInfo.getClaim("userId").asString();
                    UserDetails loginUser = userService.loadUserByUsername(userId);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            loginUser, null, loginUser.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);

                } else {
                    log.error("### TokenInfo is Null");
                }
            }
        } catch (Exception e) {
            log.error("### Filter Exception {}", e.getMessage());
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

}
