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

import java.util.List;
import java.util.Optional;
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

        UserSecurityDTO userSecurityDTO =
                new UserSecurityDTO(
                        userEntity.getUserId(),
                        userEntity.getUserPw(),
                        userEntity.getUserName(),
                        userEntity.getPhoneNum(),
                        userEntity.getZipCode(),
                        userEntity.getAddr(),
                        userEntity.getDetailAddr(),
                        //userEntity.getEmail(),
                        userEntity.isDeleteYn(),
                        false,
                        userEntity.getRoleSet()
                                .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_"+userRole.name())).collect(Collectors.toList())
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
                //.email(entity.getEmail())
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
                //.email(entity.getEmail())
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
                //.email(entity.getEmail())
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

//    public UserEntity findId(FindUserIdDto findUserIdDto) {
//
//        Boolean check = userRepository.existsByUserNameAndEmail(findUserIdDto.getUserName(), findUserIdDto.getEmail());
//
//        if(check){
//            Optional<UserEntity> user = userRepository.findByUserNameAndEmail(findUserIdDto.getUserName(), findUserIdDto.getEmail());
//            UserEntity userEntity = user.orElseThrow();
//            return userEntity;
//        }else{
//            return null;
//        }
//    }

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

        String str = getTempPassword();
        MailDto dto = new MailDto();

        //dto.setAddress(userEntity.getEmail());
        dto.setTitle("AnimalH 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. AnimalH 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");

        userEntity.changePw(passwordEncoder.encode(str));

        userRepository.save(userEntity);


        return dto;
    }

    private String getTempPassword() {

        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
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
