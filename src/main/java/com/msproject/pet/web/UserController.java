package com.msproject.pet.web;

import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.service.UserService;
import com.msproject.pet.util.JwtUtil;
import com.msproject.pet.web.dtos.FindUserIdDto;
import com.msproject.pet.web.dtos.UserDto;
import com.msproject.pet.web.dtos.UserJoinDto;
import com.msproject.pet.web.dtos.UserPwChangeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/join")
    public Long saveUser(@RequestBody @Valid UserDto userDto) throws Exception {

        UserEntity user = userService.saveUser(userDto);
        return  user.getIdx();
    }

    @GetMapping("/join")
    public void joinGET(){
        log.info("join get...");
    }

//    @PostMapping("/join")
//    public void joinPOST(UserJoinDto userJoinDto, HttpServletResponse response) throws IOException {
//
//        log.info("join post...");
//        log.info("userJoinDto: {}", userJoinDto);
//
//        try{
//            userService.join(userJoinDto);
//        }catch (Exception e){
//            String redirectUrl = String.format("http://localhost:8080/join");
//            // 리디렉션 처리
//            response.sendRedirect(redirectUrl);
//        }
//
//        String redirectUrl = String.format("http://localhost:8080/login");
//        response.sendRedirect(redirectUrl);
//    }

//    @GetMapping("/login")
//    public void loginGET(String errorCode, String logout){
//        log.info("login get...");
//        log.info("logout:" + logout);
//
//        if(logout != null){
//            log.info("user logout...");
//        }
//    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> paramMap) {

        String userId = paramMap.get("user_id");
        String userPw = paramMap.get("user_pw");

        UserDetails loginUser = userService.loadUserByUsername(userId); //userId로 정보 가져오기

        Authentication authentication = authenticationManager.authenticate(     //가져온 정보와 입력한 비밀번호로 검증
                new UsernamePasswordAuthenticationToken(loginUser, userPw)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);   // 검증 통과 후 authentication 세팅
        String accessToken = jwtUtil.createToken(loginUser.getUsername(), loginUser.getUsername());     //accessToken 생성

        Map<String, Object> result = new HashMap<>();

        result.put("user_id", loginUser.getUsername());
        result.put("user_token", accessToken);
        result.put("user_role", loginUser.getAuthorities().stream().findFirst().get().getAuthority());
        result.put("user_idx",userService.getUser(userId).getIdx());

        return ResponseEntity.ok(result);
    }

    //아이디 중복 체크
    @GetMapping("/check")
    public Boolean checkId(@RequestParam String userId){
        return userService.checkId(userId);
    }

    @GetMapping("/check/mail")
    public Boolean checkEmail(@RequestParam String email){
        return userService.checkEmail(email);
    }

    @PostMapping("/find")
    public String findId(@RequestBody FindUserIdDto findUserIdDto){

        UserEntity user = userService.findId(findUserIdDto);

        if(user == null){
            return null;
        }
        return user.getUserId();
    }

    @PostMapping("/find/pw")
    public String findPassword(@RequestParam("userEmail") String userEmail){

        UserEntity user = userService.findPw(userEmail);

        if(user == null){
            return null;
        }
        return user.getEmail();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PatchMapping("/")
    public UserEntity update(@RequestBody UserDto userDto){
        return userService.update(userDto);
    }

    @PatchMapping("/change-pw")
    public Boolean updatePw(@RequestBody UserPwChangeDto userPwChangeDto){
        return userService.updatePw(userPwChangeDto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
