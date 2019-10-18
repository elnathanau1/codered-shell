package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.util.Map;

@ShellComponent
public class ScraperCommands {
    @Autowired
    private PropertiesService propertiesService;

    @ShellMethod(value = "Run scrapers", key = "run-scrapers")
    public String runScrapers() {
        Map<String, String> dbValues = propertiesService.getDbValues();
        ProcessBuilder processBuilder = new ProcessBuilder(
                "./python/run-scrapers.sh",
                dbValues.get("host"),
                dbValues.get("port"),
                dbValues.get("name"),
                dbValues.get("username"),
                dbValues.get("password")
        ).inheritIO();
        try {
            Process p = processBuilder.start();
            return "Running scrapers!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to start scrapers";
        }
    }
}
