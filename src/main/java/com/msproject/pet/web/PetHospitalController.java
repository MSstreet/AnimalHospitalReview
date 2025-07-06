package com.msproject.pet.web;

import com.msproject.pet.entity.PetHospitalEntity;
import com.msproject.pet.model.Header;
import com.msproject.pet.model.SearchCondition;
import com.msproject.pet.service.PetHospitalService;
import com.msproject.pet.web.dtos.PetHospitalDto;
import com.msproject.pet.web.dtos.PetHospitalListReviewCountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/hospital")
@RestController
public class PetHospitalController {

    private final PetHospitalService petHospitalService;

    @PostMapping("/join")
    public PetHospitalEntity create(@RequestBody PetHospitalDto petHospitalDto) throws Exception{
        return petHospitalService.create(petHospitalDto);
    }

    @GetMapping("/list")
    public Header<List<PetHospitalListReviewCountDto>> hospitalList(
            @PageableDefault(sort = {"idx"}, size = 12) Pageable pageable,
            SearchCondition searchCondition)
    {
        return petHospitalService.getHospitalListWithReviewCount(pageable, searchCondition);
    }

    @GetMapping("/list1")
    public Header<List<PetHospitalListReviewCountDto>> hospitalList1(
            @PageableDefault(sort = {"idx"}, size = 12) Pageable pageable,
            SearchCondition searchCondition)
    {
        return petHospitalService.getHospitalListWithReviewCount1(pageable, searchCondition);
    }

    @GetMapping("/{id}")
    public PetHospitalListReviewCountDto getPetHospital(@PathVariable Long id){
        return petHospitalService.getPetHospitalWithReviewCount(id);
    }

    @PatchMapping("/")
    public PetHospitalEntity update(@RequestBody PetHospitalDto petHospitalDto){
        return petHospitalService.update(petHospitalDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        petHospitalService.delete(id);
    }
}
