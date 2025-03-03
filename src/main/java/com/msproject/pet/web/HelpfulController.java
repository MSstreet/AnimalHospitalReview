package com.msproject.pet.web;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class HelpfulController {

    private final HelpfulService helpfulService;

    @PostMapping("/")
    public HelpfulDto createHelpfulInfo(@RequestBody HelpfulDto helpfulDto){
            return helpfulService.createHelpful(helpfulDto);
    }

    @GetMapping("/check/{uid}/{rid}")
    public Boolean checkId(@PathVariable Long uid, @PathVariable Long rid){
        return helpfulService.checkWish(uid,rid);
    }

//    @GetMapping("/one/{uid}/{rid}")
//    public HelpfulDto getHelpful(@PathVariable Long uid, @PathVariable Long rid){
//        return helpfulService.getHelpful(uid,rid);
//    }
}
