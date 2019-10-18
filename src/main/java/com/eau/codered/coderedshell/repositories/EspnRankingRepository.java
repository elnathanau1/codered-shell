package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.EspnRankingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EspnRankingRepository extends CrudRepository<EspnRankingEntity, Integer> {
    EspnRankingEntity findByNameAllIgnoreCase(String name);
}
