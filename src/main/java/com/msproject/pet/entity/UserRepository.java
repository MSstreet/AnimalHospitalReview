package com.msproject.pet.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);

    //Boolean existsByEmail(String email);

    //Optional<UserEntity> findByUserNameAndEmail(String userName, String Email);

    //Boolean existsByUserNameAndEmail(String userName, String Email);

    @EntityGraph(attributePaths = "roleSet")
    @Query("select u from UserEntity u where u.userId = :userId")
    Optional<UserEntity> getWithRole(@Param("userId") String userId);

//    @EntityGraph(attributePaths = "roleSet")
//    Optional<UserEntity> findByEmail(String Email);

    @Modifying
    @Transactional
    @Query("update UserEntity u set u.userPw =:userPw where u.userId = :userId ")
    void updatePassword(@Param("userPw") String userPw, @Param("userId") String userId);



}
