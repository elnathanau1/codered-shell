package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeagueRepository extends CrudRepository<LeagueEntity, Integer> {
    public LeagueEntity findByName(String name);

    public List<LeagueEntity> findAll();
}
