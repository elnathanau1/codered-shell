package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.repositories.HashtagRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashtagRankingService {
    @Autowired
    HashtagRankingRepository hashtagRankingRepository;
}
