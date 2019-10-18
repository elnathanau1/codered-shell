package com.eau.codered.coderedshell.repositories;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DraftingRoomRepository extends CrudRepository<DraftingRoomEntity, Integer> {
    List<DraftingRoomEntity> findAllByLeague(int league);
}
