package com.eau.codered.coderedshell.config;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.LeagueEntity;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

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
    private Queue<Integer> draftOrder = new LinkedList<>();
    private List<String> playerNames = new ArrayList<>();
    private List<String> draftLog = new ArrayList();
    private Stack<DraftingRoomEntity> pastDraftingRoomEntities = new Stack<>();
    private Stack<Integer> pastDraftOrder = new Stack<>();

}
