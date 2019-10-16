package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.TeamEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends CrudRepository<TeamEntity, Integer> {
}
