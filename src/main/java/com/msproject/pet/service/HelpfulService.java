package com.msproject.pet.service;

import com.msproject.pet.entity.*;
import com.msproject.pet.repository.HelpfulRepository;
import com.msproject.pet.repository.HelpfulRepositoryCustom;
import com.msproject.pet.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HelpfulService {

    private final HelpfulRepository helpfulRepository;
    private final HelpfulRepositoryCustom helpfulRepositoryCustom;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public long getHelpfulCount(Long id) {
        return helpfulRepositoryCustom.getHelpfulCount(id);
    }
}
