package com.eau.codered.coderedshell.util;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import lombok.Getter;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.*;
import java.util.function.Function;

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

    public static String getPositionalScarcity(List<DraftingRoomEntity> draftingRoomEntities) {
        List<String> positions = Arrays.asList("pg", "sg", "sf", "pf", "c");
        List<Double> statThresholds = Arrays.asList(-2.5, 0.0, 2.5, 5.0, 7.5, 10.0, 20.0);
        List<String> bounds = new ArrayList<>();

        Map<String, Integer> data = new HashMap<>();
        for (String position : positions) {
            for (int i = 0; i < statThresholds.size() - 1; i++) {
                Double lowerBound = statThresholds.get(i);
                Double upperBound = statThresholds.get(i + 1);
                String newBounds = statThresholds.get(i).toString();

                String newEntry = position + " " + newBounds;

                Integer newValue = (int) draftingRoomEntities
                        .stream()
                        .filter(x -> Arrays.asList(x.getPos().toLowerCase().split(",")).contains(position))
                        .filter(x -> x.getTotal() > lowerBound && x.getTotal() <= upperBound)
                        .count();

                data.put(newEntry, newValue);
                bounds.add(String.valueOf(lowerBound) + " - " + String.valueOf(upperBound));
            }
        }

        List<String[]> model = new ArrayList<>();
        // TODO: FIND A BETTER WAY TO WRITE THIS
        model.add(new String[]{
                "", bounds.get(0), bounds.get(1), bounds.get(2), bounds.get(3), bounds.get(4), bounds.get(5)
        });

        // TODO: THIS IS BAD CODE. FIX.
        for (String position : positions) {
            model.add(new String[]{
                    position,
                    String.valueOf(data.get(position + " " + statThresholds.get(0))),
                    String.valueOf(data.get(position + " " + statThresholds.get(1))),
                    String.valueOf(data.get(position + " " + statThresholds.get(2))),
                    String.valueOf(data.get(position + " " + statThresholds.get(3))),
                    String.valueOf(data.get(position + " " + statThresholds.get(4))),
                    String.valueOf(data.get(position + " " + statThresholds.get(5)))
            });
        }

        String[][] array = new String[model.size()][model.get(0).length];
        for (int i = 0; i < model.size(); i++) {
            array[i] = model.get(i);
        }

        TableModel tableModel = new ArrayTableModel(array);
        TableBuilder tableBuilder = new TableBuilder(tableModel);
        tableBuilder.addFullBorder(BorderStyle.fancy_light);
        String table = tableBuilder.build().render(150);

        return table;

    }
}
