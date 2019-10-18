package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.entities.TeamEntity;
import com.eau.codered.coderedshell.services.DraftService;
import com.eau.codered.coderedshell.services.LeagueService;
import com.eau.codered.coderedshell.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Scanner;

@ShellComponent
public class LeagueCommands {
    @Autowired
    private LeagueService leagueService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private DraftService draftService;

    @Autowired
    DraftService draftService;

    @ShellMethod(value = "Create a league", key = "create-league")
    public String createLeague(@ShellOption(value = {"-n", "--name"}) String name,
                               @ShellOption(defaultValue = "12", value = {"-t", "--teams"}) int numTeams) {
        LeagueEntity leagueEntity = leagueService.createLeague(name, numTeams);

        if (leagueEntity == null) {
            return "Could not create league, choose a different name.";
        }

        for (int i = 0; i < numTeams; i++) {
            TeamEntity teamEntity = TeamEntity.builder()
                    .leagueId(leagueEntity.getId())
                    .name("Team " + i)
                    .build();
            teamService.addTeam(teamEntity);
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

    @ShellMethod(value = "Delete a league", key = "delete-league")
    public String deleteLeague(@ShellOption(value = {"-n", "--name"}) String name) {
        // confirm with user
        Scanner scanner = new Scanner(System.in);
        System.out.println("This will delete all info connected to the league. Continue? (y/n)");
        String userInput = scanner.next();

        if (!userInput.equals("y")) {
            return "delete-league cancelled.";
        }

        LeagueEntity leagueEntity = leagueService.getLeagueByName(name);

        if (!teamService.deleteTeamsInLeague(leagueEntity) || !draftService.cleanDraft(leagueEntity)|| !leagueService.deleteLeague(name)) {
            return "Failed to delete league";
        }

        return "Successfully deleted league.";

    }
}
