package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.repositories.LeagueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {
    private static Logger logger = LoggerFactory.getLogger(LeagueService.class);

    @Autowired
    LeagueRepository leagueRepository;

    public LeagueEntity createLeague(String name, int numTeams) {
        if (leagueRepository.findByName(name) == null) {
            LeagueEntity leagueEntity = LeagueEntity.builder()
                    .name(name)
                    .numTeams(numTeams)
                    .build();
            return leagueRepository.save(leagueEntity);
        }
        return null;
    }

    public boolean deleteLeague(String name) {
        try {
            leagueRepository.delete(leagueRepository.findByName(name));
            return true;
        } catch (Exception e) {
            logger.info("Could not delete league={}, exception e={}", name, e.getMessage());
            return false;
        }
    }

    public LeagueEntity getLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    public List<LeagueEntity> getAllLeagues() {
        return leagueRepository.findAll();
    }
}
