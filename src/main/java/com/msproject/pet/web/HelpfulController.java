package com.msproject.pet.web;
import com.msproject.pet.model.Header;
import com.msproject.pet.service.HelpfulService;
import com.msproject.pet.web.dtos.HelpfulDto;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/help")
public class HelpfulController {

    private final HelpfulService helpfulService;

    @GetMapping("/{id}")
    public long getHelpfulCount(@PathVariable Long id) {
        return helpfulService.getHelpfulCount(id);
    }

    @GetMapping("/no/{id}")
    public long getUnHelpfulCount(@PathVariable Long id) {
        return helpfulService.getUnHelpfulCount(id);
    }
}
