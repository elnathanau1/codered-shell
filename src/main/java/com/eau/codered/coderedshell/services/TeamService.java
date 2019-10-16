package com.eau.codered.coderedshell.services;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.entities.TeamEntity;
import com.eau.codered.coderedshell.repositories.LeagueRepository;
import com.eau.codered.coderedshell.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
    @Autowired
    TeamRepository teamRepository;

    public void addTeam(TeamEntity teamEntity) {
        teamRepository.save(teamEntity);
    }
}
