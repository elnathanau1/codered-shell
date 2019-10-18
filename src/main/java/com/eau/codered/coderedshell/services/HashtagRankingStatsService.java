package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.HashtagRankingStatsEntity;
import com.eau.codered.coderedshell.repositories.HashtagRankingStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashtagRankingStatsService {
    @Autowired
    HashtagRankingStatsRepository hashtagRankingStatsRepository;

    public List<HashtagRankingStatsEntity> getAll() {
        return hashtagRankingStatsRepository.findAll();
    }

    public HashtagRankingStatsEntity getPlayer(String name) {
        return hashtagRankingStatsRepository.findByName(name);
    }
}
