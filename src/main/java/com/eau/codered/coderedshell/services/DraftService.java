package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.config.DraftState;
import com.eau.codered.coderedshell.entities.*;
import com.eau.codered.coderedshell.repositories.DraftedPlayerRepository;
import com.eau.codered.coderedshell.repositories.DraftingRoomRepository;
import com.eau.codered.coderedshell.util.DraftUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DraftService {
    @Autowired
    private EspnRankingService espnRankingService;
    @Autowired
    private HashtagRankingZscoreService hashtagRankingZscoreService;
    @Autowired
    private HashtagRankingStatsService hashtagRankingStatsService;
    @Autowired
    private DraftingRoomRepository draftingRoomRepository;
    @Autowired
    private DraftedPlayerRepository draftedPlayerRepository;
    @Autowired
    private DraftState draftState;

    @Autowired
    private LeagueService leagueService;
    @Autowired
    private TeamService teamService;

    @Autowired
    private ModelMapper modelMapper;

    private static Logger logger = LoggerFactory.getLogger(DraftService.class);

    public void setupDraft() {
        LeagueEntity leagueEntity = draftState.getLeagueEntity();

        cleanDraft(leagueEntity);
        createDraftBoard(leagueEntity);
        setDraftOrder(leagueEntity);

        draftState.setDrafting(true);
        draftState.setBoardLength(2 * leagueEntity.getNumTeams());
    }

    private void setDraftOrder(LeagueEntity leagueEntity) {
        Queue<Integer> draftOrder = new LinkedList<Integer>();
        List<Integer> integerList = new ArrayList<>();
        for (int i = 1; i <= leagueEntity.getNumTeams(); i++) {
            integerList.add(i);
        }

        // TODO: replace 13 with num of rounds
        for (int i = 0; i < 13; i++) {
            draftOrder.addAll(integerList);
            Collections.reverse(integerList);
        }
        draftState.setDraftOrder(draftOrder);
    }

    private void createDraftBoard(LeagueEntity leagueEntity) {
        try {
            List<HashtagRankingZscoreEntity> hashtagRankingZscoreEntities = hashtagRankingZscoreService.getAll();

            List<DraftingRoomEntity> draftingRoomEntities = new ArrayList<>();
            for (HashtagRankingZscoreEntity zscoreEntity : hashtagRankingZscoreEntities) {
                DraftingRoomEntity newDraftingRoomEntity = modelMapper.map(zscoreEntity, DraftingRoomEntity.class);
                newDraftingRoomEntity.setLeague(leagueEntity.getId());

                HashtagRankingStatsEntity statsEntity = hashtagRankingStatsService.getPlayer(zscoreEntity.getName());
                if (statsEntity != null) {
                    newDraftingRoomEntity.setHashtagRank(statsEntity.getRank());
                    newDraftingRoomEntity.setPos(statsEntity.getPos());
                    newDraftingRoomEntity.setGp(statsEntity.getGp());
                    newDraftingRoomEntity.setMpg(statsEntity.getMpg());
                }

                EspnRankingEntity espnRankingEntity = espnRankingService.findByName(zscoreEntity.getName());
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

    public List<DraftingRoomEntity> getDraftBoard(String category) {
        LeagueEntity leagueEntity = draftState.getLeagueEntity();
        Map<String, Double> weights = draftState.getWeights();

        List<DraftingRoomEntity> draftingRoomEntities = draftingRoomRepository.findAllByLeague(leagueEntity.getId());

        draftingRoomEntities = draftingRoomEntities
                .stream()
                .map(x -> DraftUtil.setTotal(x, weights))
                .collect(Collectors.toList());

        draftingRoomEntities = DraftUtil.sortList(draftingRoomEntities, category);

        return draftingRoomEntities;
    }

    public DraftingRoomEntity getDraftPlayerByName(int leagueId, String playerName) {
        return draftingRoomRepository.findByLeagueAndNameAllIgnoreCase(leagueId, playerName);
    }

    public DraftedPlayerEntity draftPlayer(DraftingRoomEntity selectedPlayer) {
        LeagueEntity leagueEntity = draftState.getLeagueEntity();
        Integer teamNum = draftState.getDraftOrder().peek();
        if (teamNum == null) {
            return null;
        }

        DraftedPlayerEntity newPlayer = modelMapper.map(selectedPlayer, DraftedPlayerEntity.class);
        newPlayer.setDraftedLeague(draftState.getLeagueEntity().getId());
        newPlayer.setDraftedPos(draftState.getDraftNum());

        TeamEntity teamEntity = teamService.getTeamByDraftOrder(leagueEntity, teamNum);
        newPlayer.setDraftedTeam(teamEntity.getId());
        newPlayer.setDraftedTeamName(teamEntity.getName());

        draftedPlayerRepository.save(newPlayer);
        draftingRoomRepository.delete(selectedPlayer);

        draftState.setDraftNum(draftState.getDraftNum() + 1);
        draftState.getDraftOrder().remove();

        return newPlayer;

    }
}
