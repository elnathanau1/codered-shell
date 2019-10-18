package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.HashtagRankingZscoreEntity;
import com.eau.codered.coderedshell.repositories.HashtagRankingZscoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagRankingZscoreService {

    @Autowired
    HashtagRankingZscoreRepository hashtagRankingZscoreRepository;

    public List<HashtagRankingZscoreEntity> getAll() {
        return hashtagRankingZscoreRepository.findAll();
    }
}
