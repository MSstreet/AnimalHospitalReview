package com.msproject.pet.service;

import com.msproject.pet.entity.HelpfulEntity;
import com.msproject.pet.entity.PetHospitalEntity;
import com.msproject.pet.repository.HelpfulRepository;
import com.msproject.pet.web.dtos.HelpfulDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HelpfulService {

    private final HelpfulRepository helpfulRepository;

    public HelpfulDto createHelpful(HelpfulDto helpfulDto) {
//        if(helpfulDto.getHelpFul() == 0){
//            helpfulRepository.deleteById(helpfulDto.getIdx());
//        }else{
//            helpfulRepository.save(HelpfulEntity.of(helpfulDto,helpfulDto))
//        }
    }

}
