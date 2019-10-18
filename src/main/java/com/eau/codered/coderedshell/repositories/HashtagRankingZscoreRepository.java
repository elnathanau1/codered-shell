package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.HashtagRankingZscoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRankingZscoreRepository extends CrudRepository<HashtagRankingZscoreEntity, Integer> {
    List<HashtagRankingZscoreEntity> findAll();
}
