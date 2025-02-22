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
    public HelpfulDto createHelpfulInfo(@RequestBody HelpfulDto helpfulDto){
            return helpfulService.createHelpful(helpfulDto);
    }

    @GetMapping("/{uid}")
    public Header<List<WishDto>> helpfulList(@PathVariable Long uid,
                                          @PageableDefault(sort = {"idx"}) Pageable pageable)
    {
        return helpfulService.getHelpfulList(pageable,uid);
    }
}
