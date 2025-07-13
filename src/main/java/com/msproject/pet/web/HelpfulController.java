package com.msproject.pet.web;

import com.msproject.pet.model.Header;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/help")
@RestController
public class HelpfulController {

    private final HelpfulService helpfulService;

    @PostMapping("")
    public Header<HelpfulDto> insertHelpful(@RequestBody HelpfulDto helpfulDto){
        log.info("Received helpful request: {}", helpfulDto);
        log.info("reviewNum: {}, userNum: {}, helpFul: {}", 
                helpfulDto.getReviewNum(), helpfulDto.getUserNum(), helpfulDto.getHelpFul());
        
        try {
            Header<HelpfulDto> result = helpfulService.insertHelpful(helpfulDto);
            log.info("Helpful response: {}", result);
            return result;
        } catch (Exception e) {
            log.error("Error in insertHelpful: ", e);
            throw e;
        }
    }

    @GetMapping("/help/{id}")
    public long getHelpfulCount(@PathVariable Long id) {
        return helpfulService.getHelpfulCount(id);
    }

//    @GetMapping("/help/{uid}/{rid}")
//    public Header<HelpfulDto> getHelpful(@PathVariable Long uid, @PathVariable Long rid){
//        return helpfulService.getHelpful(uid,rid);
//    }
}
