package com.eau.codered.coderedshell.commands;

import com.eau.codered.coderedshell.config.DraftStateConfig;
import com.eau.codered.coderedshell.entities.DraftingRoomEntity;
import com.eau.codered.coderedshell.entities.LeagueEntity;
import com.eau.codered.coderedshell.services.DraftService;
import com.eau.codered.coderedshell.services.LeagueService;
import com.eau.codered.coderedshell.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@ShellComponent
public class DraftCommands {

    @Autowired
    private DraftService draftService;

    @Autowired
    private LeagueService leagueService;

    @Autowired
    private UtilCommands utilCommands;

    @Autowired
    private DraftStateConfig draftState;


    @ShellMethod(value = "Start a draft for a league", key = "start-draft")
    public String startDraft(@ShellOption(value = {"-l", "--league"}) String league) {
        // confirm with user
        Scanner scanner = new Scanner(System.in);
        System.out.println("This will delete any existing draft data. Continue? (y/n)");
        String userInput = scanner.next();

        if (!userInput.equals("y")) {
            return "start-draft cancelled.";
        }

        utilCommands.clear();

        LeagueEntity leagueEntity = leagueService.getLeagueByName(league);
        if (leagueEntity != null) {
            draftState.setLeagueEntity(leagueEntity);
            draftService.setupDraft();

            draftState.setDrafting(true);

            return "Draft set up!";
        }
        return "Enter a valid league";
    }

    @ShellMethod(value = "Display the draft board", key = {"draft-board", "db"})
    public String draftBoard(@ShellOption(value = {"-s", "--sort"}, defaultValue = "htag_rk") String sort) {
        List<String> possibleSort = new ArrayList<>();
        if (draftState.isDrafting()) {
            // display table
            List<String[]> model = new ArrayList<>();
            model.add(new String[]{
                    "htag_rk", "espn_rk", "espn_adp", "name", "pos", "gp ", "fg_pct", "ft_pct", "3pm  ", "pts  ", "reb  ", "ast  ", "stl  ", "blk  ", "to   ", "total"
            });

            List<DraftingRoomEntity> draftingRoomEntities = draftService.getDraftBoard();
            for (DraftingRoomEntity draftingRoomEntity : draftingRoomEntities) {
                model.add(new String[]{
                        String.valueOf(draftingRoomEntity.getHashtagRank()),
                        String.valueOf(draftingRoomEntity.getEspnRank()),
                        String.valueOf(draftingRoomEntity.getEspnAdp()),
                        String.valueOf(draftingRoomEntity.getName()),
                        String.valueOf(draftingRoomEntity.getPos()),
                        String.valueOf(draftingRoomEntity.getGp()),
                        String.valueOf(draftingRoomEntity.getFgPercent()),
                        String.valueOf(draftingRoomEntity.getFtPercent()),
                        String.valueOf(draftingRoomEntity.getThreepm()),
                        String.valueOf(draftingRoomEntity.getPts()),
                        String.valueOf(draftingRoomEntity.getTreb()),
                        String.valueOf(draftingRoomEntity.getAst()),
                        String.valueOf(draftingRoomEntity.getStl()),
                        String.valueOf(draftingRoomEntity.getBlk()),
                        String.valueOf(draftingRoomEntity.getTurnovers()),
                        String.valueOf(draftingRoomEntity.getTotal()),

                });
            }

            String[][] array = new String[model.size()][model.get(0).length];
            for (int i = 0; i < model.size(); i++) {
                // truncate values to make more readable
                for (int j = 0; j < model.get(0).length; j++) {
                    if (j != 3 && j != 4 && i != 0) {
                        model.get(i)[j] = StringUtil.truncate(model.get(i)[j], 4);
                    }
                }
                array[i] = model.get(i);
            }
            TableModel tableModel = new ArrayTableModel(array);
            TableBuilder tableBuilder = new TableBuilder(tableModel);
            tableBuilder.addFullBorder(BorderStyle.fancy_light);
            String table = tableBuilder.build().render(150);

            utilCommands.clear();
            return table;
        }
        return "Not currently drafting, use start-draft to begin";
    }

    @ShellMethod(value = "Set the weights for total calculations", key = {"set-weights", "sw"})
    public String setWeights(@ShellOption(value = {"-c", "--cat", "--category"}) String category,
                             @ShellOption(value = {"-v", "--value",}) Double value) {
        Map<String, Double> weights = draftState.getWeights();
        weights.put(category, value);
        draftState.setWeights(weights);

        return "Weights have been set!";
    }

    @ShellMethod(value = "Set the size of the draft board", key = {"set-draft-size", "sds"})
    public String setWeights(@ShellOption(value = {"-v", "--value",}) Integer value) {
        if (value > 0) {
            draftState.setBoardLength(value);
            return "Draft board size is now set to " + value;
        }

        return "Choose a size greater than 0";
    }
}
