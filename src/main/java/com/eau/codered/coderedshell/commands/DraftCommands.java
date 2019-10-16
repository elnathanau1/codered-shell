package com.eau.codered.coderedshell.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Scanner;

@ShellComponent
public class DraftCommands {

    @Autowired
    private UtilCommands utilCommands;

    @ShellMethod(value = "Start a draft for a league", key = "start-draft")
    public String startDraft(@ShellOption(value={"-l", "--league"}) String league) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("This will delete any existing draft data. Continue? (y/n)");
        String userInput = scanner.next();

        if (!userInput.equals("y")) {
            return "start-draft cancelled.";
        }

        utilCommands.clear();

        return "This is my clear";
    }
}
