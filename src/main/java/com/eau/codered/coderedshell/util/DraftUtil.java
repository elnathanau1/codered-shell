package com.eau.codered.coderedshell.util;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DraftUtil {
    @Getter
    private static List<String> validCategories = new ArrayList<>(Arrays.asList(
            "htag_rk", "espn_rk", "espn_adp", "name", "pos", "gp", "fg_pct", "ft_pct", "3pm", "pts", "reb", "ast", "stl", "blk", "to", "total"
    ));

    @Getter
    private static List<String> statCategories = new ArrayList<>(Arrays.asList(
            "fg_pct", "ft_pct", "3pm", "pts", "reb", "ast", "stl", "blk", "to"
    ));

    @Getter
    private static Map<String, Function<DraftingRoomEntity, Comparable>> functionMap = new HashMap<String, Function<DraftingRoomEntity, Comparable>>() {{
        put("espn_rank", DraftingRoomEntity::getEspnRank);
        put("espn_adp", DraftingRoomEntity::getEspnAdp);
        put("htag_rank", DraftingRoomEntity::getHashtagRank);
        put("name", DraftingRoomEntity::getName);
        put("pos", DraftingRoomEntity::getPos);
        put("gp", DraftingRoomEntity::getGp);
        put("mpg", DraftingRoomEntity::getMpg);
        put("fg_pct", DraftingRoomEntity::getFgPercent);
        put("ft_pct", DraftingRoomEntity::getFtPercent);
        put("3pm", DraftingRoomEntity::getThreepm);
        put("pts", DraftingRoomEntity::getPts);
        put("reb", DraftingRoomEntity::getTreb);
        put("ast", DraftingRoomEntity::getAst);
        put("stl", DraftingRoomEntity::getStl);
        put("blk", DraftingRoomEntity::getBlk);
        put("to", DraftingRoomEntity::getTurnovers);
        put("total", DraftingRoomEntity::getTotal);
    }};


    public static boolean validCategory(String category) {
        return validCategories.contains(category);
    }

    public static DraftingRoomEntity setTotal(DraftingRoomEntity orig, Map<String, Double> weights) {
        Map<String, Double> newWeights = new HashMap<String, Double>(weights);
        newWeights.put("to", weights.get("to") * -1);

        double total = statCategories
                .stream()
                .mapToDouble(x -> newWeights.get(x) * Double.parseDouble(String.valueOf(functionMap.get(x).apply(orig))))
                .sum();
        orig.setTotal(total);

        return orig;
    }

    public static List<DraftingRoomEntity> sortList(List<DraftingRoomEntity> draftingRoomEntities, String category) {
        if (!validCategory(category)) {
            return draftingRoomEntities;
        }

        draftingRoomEntities.sort(Comparator.comparing(functionMap.get(category)).reversed());

        List<String> reverseCategories = new ArrayList<String>(Arrays.asList(
                "espn_rank",
                "espn_adp",
                "htag_rank",
                "to"
        ));

        if (reverseCategories.contains(category)) {
            Collections.reverse(draftingRoomEntities);
        }

        return draftingRoomEntities;
    }
}
