package com.msproject.pet.web;

import com.msproject.pet.model.Header;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController("/help")
public class HelpfulController {

    private final HelpfulService helpfulService;

    @PostMapping("")
    public Header<HelpfulDto> insertHelpful(@RequestBody HelpfulDto helpfulDto){

        return helpfulService.insertHelpful(helpfulDto);

    }

    @GetMapping("/{id}")
    public long getHelpfulCount(@PathVariable Long id) {
        return helpfulService.getHelpfulCount(id);
    }

    @GetMapping("/helpful/{uid}/{rid}")
    public Boolean checkHelpful(@PathVariable Long uid, @PathVariable Long rid){
        return helpfulService.checkHelpful(uid,rid);
    }
}
