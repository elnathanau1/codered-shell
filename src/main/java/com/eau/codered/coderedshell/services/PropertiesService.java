package com.eau.codered.coderedshell.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PropertiesService {
    @Value("${shell.db.host}")
    private String dbHost;

    @Value("${shell.db.port}")
    private String dbPort;

    @Value("${shell.db.name}")
    private String dbName;

    @Value("${shell.db.username}")
    private String dbUser;

    @Value("${shell.db.password}")
    private String dbPassword;

    public Map<String, String> getDbValues() {
        Map<String, String> map = new HashMap<>();
        map.put("host", dbHost);
        map.put("port", dbPort);
        map.put("name", dbName);
        map.put("username", dbUser);
        map.put("password", dbPassword);

        return map;
    }
}
