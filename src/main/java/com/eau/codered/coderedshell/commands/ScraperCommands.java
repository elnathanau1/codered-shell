package com.eau.codered.coderedshell.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;

@ShellComponent
public class ScraperCommands {

    @ShellMethod(value = "Run scrapers", key = "run-scrapers")
    public String runScrapers() {
        ProcessBuilder processBuilder = new ProcessBuilder("./python/run-scrapers.sh").inheritIO();
        try {
            Process p = processBuilder.start();
            return "Running scrapers!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to start scrapers";
        }
    }
}
