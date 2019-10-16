package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.DraftedPlayerEntity;
import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.repositories.DraftedPlayerRepository;
import com.eau.codered.coderedshell.repositories.DraftingRoomRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftService {
    @Autowired
    EspnRankingService espnRankingService;
    @Autowired
    HashtagRankingService hashtagRankingService;
    @Autowired
    DraftingRoomRepository draftingRoomRepository;
    @Autowired
    DraftedPlayerRepository draftedPlayerRepository;

    @Autowired
    LeagueService leagueService;

    @Autowired
    ModelMapper modelMapper;

    private static Logger logger = LoggerFactory.getLogger(DraftService.class);

    public boolean setupDraft(String leagueName) {
        LeagueEntity leagueEntity = leagueService.getLeagueByName(leagueName);

        cleanDraft(leagueEntity);
        return false;

    }

    /**
     * Delete all draft info in db associated with league
     *
     * @param leagueEntity
     */
    public boolean cleanDraft(LeagueEntity leagueEntity) {
        try {
            List<DraftingRoomEntity> draftingRoomEntities = draftingRoomRepository.findAllByLeague(leagueEntity.getId());
            draftingRoomRepository.deleteAll(draftingRoomEntities);

            List<DraftedPlayerEntity> draftedPlayerEntities = draftedPlayerRepository.findAllByDraftedLeague(leagueEntity.getId());
            draftedPlayerRepository.deleteAll(draftedPlayerEntities);

            return true;
        } catch (Exception e) {
            logger.error("Encountered exception={}", e.getMessage());
            return false;
        }
    }
}
