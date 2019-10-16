package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.repositories.LeagueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {
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

    public LeagueEntity getLeagueByName(String name) {
        return leagueRepository.findByName(name);
    }

    public List<LeagueEntity> getAllLeagues() {
        return leagueRepository.findAll();
    }
}
