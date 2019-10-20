package com.eau.codered.coderedshell.providers;

import com.eau.codered.coderedshell.util.DraftUtil;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SortValueProvider extends ValueProviderSupport {

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {

        return DraftUtil.getValidCategories().stream().map(String::toLowerCase).map(CompletionProposal::new).collect(Collectors.toList());

    }

}
