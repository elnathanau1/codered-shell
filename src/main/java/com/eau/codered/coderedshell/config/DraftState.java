package com.eau.codered.coderedshell.config;

import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.entities.TeamEntity;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Service
public class DraftState {
    // state
    private boolean drafting = false;
    private LeagueEntity leagueEntity = null;
    private Map<String, Double> weights = new HashMap<String, Double>() {{
        put("fg_pct", 1.0);
        put("ft_pct", 1.0);
        put("3pm", 1.0);
        put("pts", 1.0);
        put("reb", 1.0);
        put("ast", 1.0);
        put("stl", 1.0);
        put("blk", 1.0);
        put("to", 0.25);
    }};
    private int boardLength = 24;
    private String sortCategory = "total";
    private int draftNum = 1;
    private int draftPos = 1;
    private boolean draftForward = true;
}
