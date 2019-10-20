package com.eau.codered.coderedshell.providers;

import com.eau.codered.coderedshell.config.DraftState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlayerValueProvider extends ValueProviderSupport {

    @Autowired
    private DraftState draftState;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {

        return draftState.getPlayerNames().stream().map(String::toLowerCase).map(CompletionProposal::new).collect(Collectors.toList());

    }

}
