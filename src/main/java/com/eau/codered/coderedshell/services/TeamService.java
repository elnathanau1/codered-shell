package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.entities.TeamEntity;
import com.eau.codered.coderedshell.repositories.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private static Logger logger = LoggerFactory.getLogger(TeamService.class);

    @Autowired
    TeamRepository teamRepository;

    public void addTeam(TeamEntity teamEntity) {
        teamRepository.save(teamEntity);
    }

    public boolean deleteTeamsInLeague(LeagueEntity leagueEntity) {
        try {
            List<TeamEntity> teamEntities = teamRepository.findAllByLeagueId(leagueEntity.getId());
            teamRepository.deleteAll(teamEntities);
            return true;
        } catch (Exception e) {
            logger.info("Could not delete teams, exception e={}", e.getMessage());
            return false;
        }
    }

    public List<TeamEntity> getTeamsByLeagueId(LeagueEntity leagueEntity) {
        return teamRepository.findAllByLeagueIdOrderByDraftOrder(leagueEntity.getId());
    }

    public TeamEntity getTeamByDraftOrder(LeagueEntity leagueEntity, int draftOrder) {
        return teamRepository.findByLeagueIdAndDraftOrder(leagueEntity.getId(), draftOrder);
    }
    public void saveTeams(List<TeamEntity> teams) {
        teamRepository.saveAll(teams);
    }
}
