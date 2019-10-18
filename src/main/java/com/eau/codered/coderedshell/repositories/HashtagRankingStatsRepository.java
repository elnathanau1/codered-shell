package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.HashtagRankingStatsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRankingStatsRepository extends CrudRepository<HashtagRankingStatsEntity, Integer> {
    List<HashtagRankingStatsEntity> findAll();

    HashtagRankingStatsEntity findByNameAllIgnoreCase(String name);
}
