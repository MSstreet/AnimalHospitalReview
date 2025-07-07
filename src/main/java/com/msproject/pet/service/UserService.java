package com.msproject.pet.service;

import com.msproject.pet.entity.*;
import com.msproject.pet.repository.BoardReplyRepository;
import com.msproject.pet.repository.ReviewRepository;
import com.msproject.pet.repository.UserHistoryRepository;
import com.msproject.pet.security.UserSecurityDTO;
import com.msproject.pet.web.dtos.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    private final UserRepositoryCustom userRepositoryCustom;
    private final UserHistoryRepository userHistoryRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    private final BoardReplyRepository boardReplyRepository;
    private final ModelMapper modelMapper;
    private final JavaMailSender mailSender;

    @Transactional
    public UserEntity saveUser(UserDto userDto) throws Exception{
        String userId = userDto.getUserId();
        boolean exist = userRepository.existsByUserId(userId);
        if(exist){
            throw new RuntimeException();
        }

        validateDuplicateEmail(userDto.getUserId());

        UserEntity userEntity = UserEntity.builder()
                .userId(userDto.getUserId())
                .userPw(passwordEncoder.encode(userDto.getUserPw()))
                .userName(userDto.getUserName())
                .phoneNum(userDto.getPhoneNum())
                .zipCode(userDto.getZipCode())
                .addr(userDto.getAddr())
                //.email(userDto.getEmail())
                .detailAddr(userDto.getDetailAddr())
                .build();

        userEntity.addRole(UserRole.USER);

        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadUserByUsername" + username);

        Optional<UserEntity> user = userRepository.getWithRole(username);

        if(user.isEmpty()){
            throw new UsernameNotFoundException("username not found");
        }

        UserEntity userEntity = user.get();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(
                userEntity.getUserId(),
                userEntity.getUserPw(),
                userEntity.getUserName(),
                userEntity.getPhoneNum(),
                userEntity.getZipCode(),
                userEntity.getAddr(),
                userEntity.getDetailAddr(),
                userEntity.isDeleteYn(),
                userEntity.isSocial(), // social 정보가 있다면 이 값을 사용
                Optional.ofNullable(userEntity.getRoleSet())
                        .orElse(Collections.emptySet())  // null일 경우 빈 Set으로 초기화
                        .stream()
                        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                        .collect(Collectors.toList())
                );

        log.info("userSecurityDTO" + userSecurityDTO);

        return userSecurityDTO;
    }

    //, get, update, delete
    public UserEntity update(UserDto userDto){
        UserEntity entity = userRepository.findByUserId(userDto.getUserId()).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));

        entity.change(userDto.getUserName(),userDto.getPhoneNum(),userDto.getZipCode(), userDto.getAddr(), userDto.getDetailAddr());

        return userRepository.save(entity);
    }

    public UserEntity socialUpdate(UserDto userDto){
        UserEntity entity = userRepository.findUserEntityByIdx(userDto.getIdx()).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));

        entity.joinUpdate(passwordEncoder.encode(userDto.getUserPw()),userDto.getPhoneNum(),userDto.getZipCode(), userDto.getAddr(), userDto.getDetailAddr());

        return userRepository.save(entity);
    }

    public void delete(Long id){

        UserEntity entity = userRepository.findById(id).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));

        List<ReviewEntity> reviewEntities = reviewRepository.findByUserEntity(entity);
        for (ReviewEntity entity1 : reviewEntities) {
            entity1.changeDeleteState();
        }
        List<BoardEntity> boardEntities = boardRepository.findByUserEntity(entity);
        for (BoardEntity boardEntity : boardEntities) {
            boardEntity.changeDeleteState();
        }
        List<BoardReply> boardReplies = boardReplyRepository.findByUserEntity(entity);

        for (BoardReply boardReply : boardReplies){
            boardReplyRepository.delete(boardReply);
        }

        UserHistory userHistory = UserHistory.builder()
                .userId(entity.getUserId())
                .userPw(entity.getUserPw())
                .userName(entity.getUserName())
                .phoneNum(entity.getPhoneNum())
                .zipCode(entity.getZipCode())
                .addr(entity.getAddr())
                .detailAddr(entity.getDetailAddr())
                .build();

        userHistoryRepository.save(userHistory);

        userRepositoryCustom.delete(entity.getIdx());
    }

    public Boolean updatePw(UserPwChangeDto userPwChangeDto) {
        Optional<UserEntity> user = userRepository.findById(userPwChangeDto.getIdx());
        UserEntity userEntity = user.orElseThrow();

        if(passwordEncoder.matches(userPwChangeDto.getPassword(),userEntity.getUserPw())){
            userEntity.changePw(passwordEncoder.encode(userPwChangeDto.getNewPassword()));
            userRepository.save(userEntity);

            return true;
        }else{
            return false;
        }

    }

    public UserDto getUser(Long id){
        UserEntity entity = userRepository.findById(id).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));

        return UserDto.builder()
                .idx(entity.getIdx())
                .userId(entity.getUserId())
                .userPw(entity.getUserPw())
                .userName(entity.getUserName())
                .phoneNum(entity.getPhoneNum())
                .zipCode(entity.getZipCode())
                .addr(entity.getAddr())
                .detailAddr(entity.getDetailAddr())
                .build();
    }

    public UserDto getUser(String userId){
        UserEntity entity = userRepository.findByUserId(userId).orElseThrow(()->new RuntimeException("존재하지 않는 유저입니다."));

        return UserDto.builder()
                .idx(entity.getIdx())
                .userId(entity.getUserId())
                .userPw(entity.getUserPw())
                .userName(entity.getUserName())
                .phoneNum(entity.getPhoneNum())
                .zipCode(entity.getZipCode())
                .addr(entity.getAddr())
                .detailAddr(entity.getDetailAddr())
                .build();
    }

    private void validateDuplicateEmail(String userId) {

    }

    public Boolean checkId(String userId){
        return userRepository.existsByUserId(userId);
     }

    public Boolean checkEmail(String email) {
        return userRepository.existsByUserId(email);
    }

    public UserEntity findPw(String userEmail) {
        Boolean check = userRepository.existsByUserId(userEmail);

        if(check){
            Optional<UserEntity> user = userRepository.findByUserId(userEmail);
            UserEntity userEntity = user.orElseThrow();

            MailDto mailDto = createMailAndChangePassword(userEntity);
            mailSend(mailDto);

            return userEntity;

        }else{
            return null;
        }
    }

    public MailDto createMailAndChangePassword(UserEntity userEntity) {
        String tempPassword = generateSecureTempPassword();
        MailDto dto = new MailDto();

        dto.setTitle("[AnimalH] 임시 비밀번호 발급 안내");
        dto.setMessage(createPasswordResetEmailTemplate(userEntity.getUserName(), tempPassword));

        userEntity.changePw(passwordEncoder.encode(tempPassword));
        userRepository.save(userEntity);

        return dto;
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

    private String createPasswordResetEmailTemplate(String userName, String tempPassword) {
        return String.format(
            "안녕하세요, %s님!\n\n" +
            "AnimalH에서 임시 비밀번호 발급 요청을 받았습니다.\n\n" +
            "▣ 임시 비밀번호: %s\n\n" +
            "보안을 위해 로그인 후 반드시 비밀번호를 변경해주세요.\n" +
            "임시 비밀번호는 24시간 후 만료됩니다.\n\n" +
            "감사합니다.\n" +
            "AnimalH 팀",
            userName, tempPassword
        );
    }

    public void mailSend(MailDto mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("kssjjh123@gmail.com");
        message.setReplyTo("kssjjh123@gmail.com");
        System.out.println("message"+message);
        mailSender.send(message);
    }
}
