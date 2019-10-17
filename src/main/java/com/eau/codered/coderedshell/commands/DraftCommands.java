package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.services.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Scanner;

@ShellComponent
public class DraftCommands {

    @Autowired
    DraftService draftService;

    @Autowired
    private UtilCommands utilCommands;

    @ShellMethod(value = "Start a draft for a league", key = "start-draft")
    public String startDraft(@ShellOption(value={"-l", "--league"}) String league) {
        // confirm with user
        Scanner scanner = new Scanner(System.in);
        System.out.println("This will delete any existing draft data. Continue? (y/n)");
        String userInput = scanner.next();

        if (!userInput.equals("y")) {
            return "start-draft cancelled.";
        }

        utilCommands.clear();

        draftService.setupDraft(league);



        return "This is my clear";
    }
}
