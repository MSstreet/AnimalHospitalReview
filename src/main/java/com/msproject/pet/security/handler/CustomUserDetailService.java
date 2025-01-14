package com.msproject.pet.security.handler;

import com.msproject.pet.entity.UserEntity;
import com.msproject.pet.entity.UserRepository;
import com.msproject.pet.security.UserSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

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
                        userEntity.getEmail(),
                        userEntity.isDeleteYn(),
                        false,
                        userEntity.getRoleSet()
                                .stream().map(userRole -> new SimpleGrantedAuthority("ROLE_"+userRole.name())).collect(Collectors.toList())
                );

        log.info("userSecurityDTO" + userSecurityDTO);

        return userSecurityDTO;
    }

}
