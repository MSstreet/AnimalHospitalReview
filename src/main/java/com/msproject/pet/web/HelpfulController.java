package com.msproject.pet.web;

import com.msproject.pet.model.Header;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class HelpfulController {

    private final HelpfulService helpfulService;

    @PostMapping("/help")
    public Header<HelpfulDto> insertHelpful(@RequestBody HelpfulDto helpfulDto){
        return helpfulService.insertHelpful(helpfulDto);
    }

    @GetMapping("/help/{id}")
    public long getHelpfulCount(@PathVariable Long id) {
        return helpfulService.getHelpfulCount(id);
    }

    @GetMapping("/help/{uid}/{rid}")
    public Header<HelpfulDto> checkHelpful(@PathVariable Long uid, @PathVariable Long rid){
        return helpfulService.checkHelpful(uid,rid);
    }
}
