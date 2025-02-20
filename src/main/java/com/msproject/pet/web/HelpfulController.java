package com.msproject.pet.web;


import com.msproject.pet.entity.HelpfulEntity;
import com.msproject.pet.entity.WishEntity;
import com.msproject.pet.model.Header;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;
import com.msproject.pet.web.dtos.WishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HelpfulController {

    private final HelpfulService helpfulService;

    @PostMapping("/")
    public HelpfulEntity createHelpfulInfo(@RequestBody HelpfulDto helpfulDto){
        if(checkId(helpfulDto.getUserNum(),helpfulDto.getReviewNum())){
            return helpfulService.helpfulCreate(helpfulDto);
        }else{
            return update(helpfulDto);
        }
    }

    @GetMapping("/check/{uid}/{hid}")
    public Boolean checkId(@PathVariable Long uId, @PathVariable Long rId){
        return helpfulService.checkWish(uId,rId);
    }

    @PatchMapping("/")
    public WishEntity update(@RequestBody HelpfulDto helpfulDto){
        return helpfulService.update(helpfulDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        helpfulService.delete(id);
    }


    @GetMapping("/{uid}")
    public Header<List<WishDto>> helpfulList(@PathVariable Long uid,
                                          @PageableDefault(sort = {"idx"}) Pageable pageable)
    {
        return helpfulService.getHelpfulList(pageable,uid);
    }
}
