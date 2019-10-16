package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.DraftedPlayerEntity;
import com.eau.codered.coderedshell.entities.EspnRankingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftedPlayerRepository extends CrudRepository<DraftedPlayerEntity, Integer> {
    List<DraftedPlayerEntity> findAllByDraftedLeague(int draftedLeague);
}
