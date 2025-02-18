package com.msproject.pet.repository;


import com.msproject.pet.entity.HelpfulEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelpfulRepository extends JpaRepository<HelpfulEntity,Long> {
}
