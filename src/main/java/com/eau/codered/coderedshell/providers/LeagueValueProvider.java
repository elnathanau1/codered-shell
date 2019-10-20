package com.eau.codered.coderedshell.providers;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.services.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeagueValueProvider extends ValueProviderSupport {

    @Autowired
    LeagueService leagueService;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<LeagueEntity> leagues = leagueService.getAllLeagues();
        if (leagues == null) {
            return null;
        }
        List<String> leagueNames = leagues.stream().map(LeagueEntity::getName).collect(Collectors.toList());

        return leagueNames.stream().map(CompletionProposal::new).collect(Collectors.toList());

    }
}
