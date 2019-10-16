package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.HashtagRankingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRankingRepository extends CrudRepository<HashtagRankingEntity, Integer> {

}
