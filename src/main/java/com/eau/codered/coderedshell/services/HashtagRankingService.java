package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.HashtagRankingEntity;
import com.eau.codered.coderedshell.repositories.HashtagRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagRankingService {
    @Autowired
    HashtagRankingRepository hashtagRankingRepository;

    public List<HashtagRankingEntity> getAll() {
        return hashtagRankingRepository.findAll();
    }
}
