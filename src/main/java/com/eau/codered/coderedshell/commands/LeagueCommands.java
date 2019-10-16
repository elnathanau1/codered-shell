package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.services.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
public class LeagueCommands {
    @Autowired
    LeagueService leagueService;


    @ShellMethod(value = "Create a league", key = "create-league")
    public String createLeague(@ShellOption(value = {"-n", "--name"}) String name,
                               @ShellOption(defaultValue = "12", value = {"-t", "--teams"}) int numTeams) {
        LeagueEntity leagueEntity = leagueService.createLeague(name, numTeams);
        if (leagueEntity == null) {
            return "Could not create league, choose a different name.";
        }
        return "Created " + numTeams + "-team league " + name;
    }

    @ShellMethod(value = "Get leagues in db", key = "get-leagues")
    public String getLeagues() {
        List<LeagueEntity> leagueEntities = leagueService.getAllLeagues();
        StringBuffer stringBuffer = new StringBuffer("id : name : numTeams\n");
        for (LeagueEntity leagueEntity : leagueEntities) {
            stringBuffer.append(String.valueOf(
                    leagueEntity.getId()) + " : "
                    + leagueEntity.getName() + " : "
                    + String.valueOf(leagueEntity.getNumTeams()) + "\n"
            );
        }
        return stringBuffer.toString();
    }
}
