package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.HashtagRankingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRankingRepository extends CrudRepository<HashtagRankingEntity, Integer> {
    List<HashtagRankingEntity> findAll();
}
