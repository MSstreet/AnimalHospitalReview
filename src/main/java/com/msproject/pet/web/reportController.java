package com.msproject.pet.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
public class reportController {

    @PostMapping("/{reviewId}")
    public void createReport(@PathVariable Long reviewId){
        log.info(reviewId.toString());
    }

}
