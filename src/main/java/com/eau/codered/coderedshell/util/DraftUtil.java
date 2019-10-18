package com.eau.codered.coderedshell.util;

import com.eau.codered.coderedshell.entities.DraftingRoomEntity;

import java.util.Map;

public class DraftUtil {

    public static DraftingRoomEntity setTotal(DraftingRoomEntity orig, Map<String, Double> weights) {
        double total = 0.0;

        if (!weights.containsKey("fg_pct")) {
            total += orig.getFgPercent();
        } else {
            total += weights.get("fg_pct") * orig.getFgPercent();
        }

        if (!weights.containsKey("ft_pct")) {
            total += orig.getFtPercent();
        } else {
            total += weights.get("ft_pct") * orig.getFtPercent();
        }

        if (!weights.containsKey("3pm")) {
            total += orig.getThreepm();
        } else {
            total += weights.get("3pm") * orig.getThreepm();
        }

        if (!weights.containsKey("pts")) {
            total += orig.getPts();
        } else {
            total += weights.get("pts") * orig.getPts();
        }

        if (!weights.containsKey("reb")) {
            total += orig.getTreb();
        } else {
            total += weights.get("reb") * orig.getTreb();
        }

        if (!weights.containsKey("ast")) {
            total += orig.getAst();
        } else {
            total += weights.get("ast") * orig.getAst();
        }

        if (!weights.containsKey("stl")) {
            total += orig.getStl();
        } else {
            total += weights.get("stl") * orig.getStl();
        }

        if (!weights.containsKey("blk")) {
            total += orig.getBlk();
        } else {
            total += weights.get("blk") * orig.getBlk();
        }

        if (!weights.containsKey("to")) {
            total -= 0.25 * orig.getTurnovers();
        } else {
            total -= weights.get("to") * orig.getTurnovers();
        }

        orig.setTotal(total);

        return orig;
    }
}
