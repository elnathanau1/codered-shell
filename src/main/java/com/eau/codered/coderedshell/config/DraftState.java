package com.eau.codered.coderedshell.config;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@Service
public class DraftState {
    // state
    private boolean drafting = false;
    private LeagueEntity leagueEntity = null;
    private Map<String, Double> weights = new HashMap<String, Double>();
    private int boardLength = 20;
}
