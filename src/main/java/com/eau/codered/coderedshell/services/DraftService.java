package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.*;
import com.eau.codered.coderedshell.repositories.DraftedPlayerRepository;
import com.eau.codered.coderedshell.repositories.DraftingRoomRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DraftService {
    @Autowired
    private EspnRankingService espnRankingService;
    @Autowired
    private HashtagRankingService hashtagRankingService;
    @Autowired
    private DraftingRoomRepository draftingRoomRepository;
    @Autowired
    private DraftedPlayerRepository draftedPlayerRepository;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private ModelMapper modelMapper;

    private static Logger logger = LoggerFactory.getLogger(DraftService.class);

    public void setupDraft(String leagueName) {
        LeagueEntity leagueEntity = leagueService.getLeagueByName(leagueName);

        cleanDraft(leagueEntity);

        createDraftBoard(leagueEntity);

    }

    private void createDraftBoard(LeagueEntity leagueEntity) {
        try {
            List<HashtagRankingEntity> hashtagRankingEntities = hashtagRankingService.getAll();

            List<DraftingRoomEntity> draftingRoomEntities = new ArrayList<>();
            for (HashtagRankingEntity hashtagRankingEntity : hashtagRankingEntities) {
                DraftingRoomEntity newDraftingRoomEntity = modelMapper.map(hashtagRankingEntity, DraftingRoomEntity.class);
                newDraftingRoomEntity.setLeague(leagueEntity.getId());

                EspnRankingEntity espnRankingEntity = espnRankingService.findByName(hashtagRankingEntity.getName());
                if (espnRankingEntity != null) {
                    newDraftingRoomEntity.setEspnAdp(espnRankingEntity.getAdp());
                    newDraftingRoomEntity.setEspnRank(espnRankingEntity.getRank());
                }

                draftingRoomEntities.add(newDraftingRoomEntity);
            }

            draftingRoomRepository.saveAll(draftingRoomEntities);

            logger.info("Draft room is now set up");
        } catch (Exception e) {
            logger.error("Could not create draft board for league={}, exception e={}", leagueEntity, e.getMessage());
        }
    }

    public boolean cleanDraft(LeagueEntity leagueEntity) {
        try {
            List<DraftingRoomEntity> draftingRoomEntities = draftingRoomRepository.findAllByLeague(leagueEntity.getId());
            draftingRoomRepository.deleteAll(draftingRoomEntities);

            List<DraftedPlayerEntity> draftedPlayerEntities = draftedPlayerRepository.findAllByDraftedLeague(leagueEntity.getId());
            draftedPlayerRepository.deleteAll(draftedPlayerEntities);

            logger.info("Cleaned database of old draft data");
            return true;

        } catch (Exception e) {

            logger.error("Encountered exception={}", e.getMessage());
            return false;
        }
    }

    public List<DraftingRoomEntity> getDraftBoard(String leagueName) {
        LeagueEntity leagueEntity = leagueService.getLeagueByName(leagueName);
        return draftingRoomRepository.findTop20ByLeagueOrderByHashtagRank(leagueEntity.getId());
    }
}
