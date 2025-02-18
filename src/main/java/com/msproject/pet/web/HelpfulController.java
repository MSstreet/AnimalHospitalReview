package com.msproject.pet.web;


import com.msproject.pet.entity.HelpfulEntity;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Controller
public class HelpfulController {


    private final HelpfulService helpfulService;

    @PostMapping("/")
    public HelpfulEntity createHelpfulInfo(@RequestBody HelpfulDto helpfulDto){


    }
}
