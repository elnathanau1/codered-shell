package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.config.DraftState;
import com.eau.codered.coderedshell.entities.*;
import com.eau.codered.coderedshell.repositories.DraftedPlayerRepository;
import com.eau.codered.coderedshell.repositories.DraftingRoomRepository;
import com.eau.codered.coderedshell.util.DraftUtil;
import com.eau.codered.coderedshell.util.StringUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
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

            List<String> playerNames = draftingRoomEntities
                    .stream()
                    .map(DraftingRoomEntity::getName)
                    .collect(Collectors.toList());

            draftState.setPlayerNames(playerNames);

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
        newPlayer.setTotal(DraftUtil.setTotal(selectedPlayer, draftState.getWeights()).getTotal());
        newPlayer.setDraftedLeague(draftState.getLeagueEntity().getId());
        newPlayer.setDraftedPos(draftState.getDraftNum());

        TeamEntity teamEntity = teamService.getTeamByDraftOrder(leagueEntity, teamNum);
        newPlayer.setDraftedTeam(teamEntity.getId());
        newPlayer.setDraftedTeamName(teamEntity.getName());

        draftedPlayerRepository.save(newPlayer);
        draftingRoomRepository.delete(selectedPlayer);
        draftState.getPlayerNames().remove(selectedPlayer.getName());
        draftState.getPastDraftingRoomEntities().push(selectedPlayer);
        draftState.getPastDraftOrder().push(teamNum);

        draftState.getDraftLog().add(teamEntity.getName() + " drafted " + newPlayer.getName() + " with pick #" + draftState.getDraftNum());
        draftState.setDraftNum(draftState.getDraftNum() + 1);
        draftState.getDraftOrder().remove();

        return newPlayer;

    }

    public void undoDraft() {
        DraftingRoomEntity lastPlayer = draftState.getPastDraftingRoomEntities().pop();
        if (lastPlayer != null) {
            draftingRoomRepository.save(lastPlayer);

            DraftedPlayerEntity draftedPlayerEntity = draftedPlayerRepository.findByDraftedLeagueAndName(draftState.getLeagueEntity().getId(), lastPlayer.getName());
            if (draftedPlayerEntity != null) {
                draftedPlayerRepository.delete(draftedPlayerEntity);
                System.out.println("Undo - " + draftedPlayerEntity.getDraftedTeamName() + " drafted " + draftedPlayerEntity.getName() +
                        " with pick #" + draftedPlayerEntity.getDraftedPos());
            }

            draftState.getPlayerNames().add(lastPlayer.getName());
        }

        Queue<Integer> newDraftOrder = new LinkedList<>();
        newDraftOrder.add(draftState.getPastDraftOrder().pop());
        newDraftOrder.addAll(draftState.getDraftOrder());
        draftState.setDraftOrder(newDraftOrder);

        draftState.setDraftNum(draftState.getDraftNum() - 1);

        if (lastPlayer == null) {
            System.out.println("Undo - pick skipped at pick #" + draftState.getDraftNum());
        }
    }

    public Map<TeamEntity, List<String>> getTeamStats(LeagueEntity leagueEntity) {
        DecimalFormat df = new DecimalFormat("#.###");
        Map<TeamEntity, List<DraftedPlayerEntity>> teamPlayers = getAllDraftedTeams(leagueEntity);

        Map<TeamEntity, List<String>> teamStats = new HashMap<>();
        for (TeamEntity teamEntity : teamPlayers.keySet()) {
            List<Double> stats = new ArrayList<>();
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getFgPercent).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getFtPercent).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getThreepm).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getPts).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTreb).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getAst).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getStl).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getBlk).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTurnovers).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTotal).sum());

            List<String> formattedStats = stats.stream().map(df::format).collect(Collectors.toList());

            teamStats.put(teamEntity, formattedStats);
        }

        return teamStats;
    }

    public Map<TeamEntity, List<DraftedPlayerEntity>> getAllDraftedTeams(LeagueEntity leagueEntity) {
        List<TeamEntity> teams = teamService.getTeamsByLeagueId(leagueEntity);

        Map<TeamEntity, List<DraftedPlayerEntity>> draftedTeams = teams.stream().collect(Collectors.toMap(team -> team, this::getDraftedTeam, (a, b) -> b));

        return draftedTeams;

    }

    public List<DraftedPlayerEntity> getDraftedTeam(TeamEntity teamEntity) {
        return draftedPlayerRepository.findAllByDraftedTeam(teamEntity.getId());
    }

    public void draftPass(LeagueEntity leagueEntity) {
        TeamEntity teamEntity = teamService.getTeamByDraftOrder(leagueEntity, draftState.getDraftOrder().peek());
        draftState.getDraftLog().add(teamEntity.getName() + " passed their draft pick at #" + draftState.getDraftNum());
        draftState.setDraftNum(draftState.getDraftNum() + 1);
        draftState.getPastDraftOrder().push(draftState.getDraftOrder().poll());
        draftState.getPastDraftingRoomEntities().push(null);
    }

    public String getTeamRank(LeagueEntity leagueEntity) {
        Map<TeamEntity, List<DraftedPlayerEntity>> teamPlayers = getAllDraftedTeams(leagueEntity);

        Map<TeamEntity, List<Double>> teamStats = new HashMap<>();
        for (TeamEntity teamEntity : teamPlayers.keySet()) {
            List<Double> stats = new ArrayList<>();
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getFgPercent).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getFtPercent).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getThreepm).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getPts).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTreb).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getAst).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getStl).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getBlk).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTurnovers).sum());
            stats.add(teamPlayers.get(teamEntity).stream().mapToDouble(DraftedPlayerEntity::getTotal).sum());

            teamStats.put(teamEntity, stats);
        }
        List<TeamEntity> teams = teamService.getTeamsByLeagueId(leagueEntity);
        List<String> teamNames = teams.stream().map(TeamEntity::getName).collect(Collectors.toList());
        List<String> header = new ArrayList<>(Arrays.asList(""));
        header.addAll(teamNames);

        String[] headerArray = new String[draftState.getLeagueEntity().getNumTeams() + 1];
        header.toArray(headerArray);

        List<String[]> model = new ArrayList<>();
        model.add(headerArray);
        List<String> categories = new ArrayList<>(DraftUtil.getStatCategories());
        categories.add("total");

        for (int i = 0; i < categories.size(); i++) {
            int finalI = i;     // for some reason, this is needed.
            List<TeamEntity> unorderedTeams = new ArrayList<>(teams);
            List<TeamEntity> orderedTeams = new ArrayList<>(teams);
            orderedTeams.sort(Comparator.comparing(x -> teamStats.get(x).get(finalI)).reversed());
            List<String> teamOrder = unorderedTeams
                    .stream()
                    .map(x -> orderedTeams.indexOf(x) + 1)
                    .map(String::valueOf)
                    .collect(Collectors.toList());

            List<String> toAdd = new ArrayList<>();
            toAdd.add(categories.get(finalI));
            toAdd.addAll(teamOrder);
            String[] newEntry = new String[draftState.getLeagueEntity().getNumTeams() + 1];
            toAdd.toArray(newEntry);
            model.add(newEntry);
        }

        return StringUtil.listToTable(model);
    }
}
